package io.swagger.service;

import io.swagger.dao.RepositoryTransaction;
import io.swagger.model.Account;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.*;

@Service
public class TransactionApiService {

    @Autowired
    private RepositoryTransaction repositoryTransaction;

    @Autowired
    private AccountApiService accountApiService;

    @Autowired
    private  UserApiService userApiService;

    public TransactionApiService() {
    }

    public List<Transaction> getTransactions() {
        return (List<Transaction>) repositoryTransaction.findAll();
    }

    public Transaction getTransaction(Long transactionId) {
        return repositoryTransaction.findById(transactionId).get();
    }

    public Boolean makeTransaction(Transaction transaction) {
            /// Use account services to
            /// Find account receiver & sender
            Account receiver = accountApiService.getAccountByIBAN(transaction.getIbanReceiver());
            Account sender = accountApiService.getAccountByIBAN(transaction.getIbanSender());

            // Calculate new balance for account receiver
            Double Rbalance = receiver.getBalance();
            Double Sbalance = sender.getBalance();

            Rbalance += transaction.getTransferAmount();
            Sbalance -= transaction.getTransferAmount();

            receiver.setBalance(Rbalance);
            sender.setBalance(Sbalance);

            // UPDATE accounts in database with included new balance
            accountApiService.updateNewBalanceServiceAccounts(receiver.getBalance(), receiver.getIBAN());
            accountApiService.updateNewBalanceServiceAccounts(sender.getBalance(), sender.getIBAN());

            // now the transaction was successful save the transaction
            repositoryTransaction.save(transaction);
            return true;
    }


    public void checkValidTransaction(Transaction body) throws Exception {
        Account accountSender = accountApiService.getAccountByIBAN(body.getIbanSender());
        Account accountReceiver = accountApiService.getAccountByIBAN(body.getIbanReceiver());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User)authentication.getPrincipal();

            // validate this account belongs to the logged in user,
        if(!accountSender.getUserId().equals(loggedInUser.getId())) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Transactions from: " + body.getIbanSender() + " is not account of: " + loggedInUser.getEmail());
        }

        // validate the account is still active and the receivers account is active
        if (accountSender.getStatus() != Account.StatusEnum.ACTIVE || accountReceiver.getStatus() != Account.StatusEnum.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Transactions from: " + body.getIbanSender() + " is not account of: " + loggedInUser.getEmail());
        }

        if ((accountSender == null) || accountReceiver == null) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Account sender or receiver does not exists!");
        }

        //	The maximum amount per transaction cannot be higher than a predefined number, referred to a transaction limit
        if (loggedInUser.getRank() == User.RankEnum.CUSTOMER) {
            this.validateDailyLimit(accountSender, body.getTransferAmount());
        }

        if (body.getTransferAmount() > accountSender.getTransferLimit()) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Transfer amount is more than the limit");
        }

        // Make more transaction daily limit
        if ((body.getTransferAmount() < 0) || (body.getTransferAmount() >= accountSender.getDailyLimit())) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Transaction must be between 0 and " + accountSender.getDailyLimit().toString() + "!");
            //    Transaction must be between 0 and the daily limit
        }
        //     Account has not enough money
        else if (body.getTransferAmount() > (accountSender.getBalance() - 500)) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Sender account has not enough money");
        }

        // Checks if accounts are from type saving and if so, has same owner then proceed transaction,
        if (!validateAccountForAccountByType(accountSender, accountReceiver)) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Transactions from or to a savings account must be from the owner");
        }
    }

    // Checks if account is a saving and if so is this account from same owner then allowed to proceed,
    private Boolean validateAccountForAccountByType(Account sender, Account receiver) {
        if((sender.getRank() == Account.RankEnum.SAVING) || (receiver.getRank() == Account.RankEnum.SAVING))
        {
            if(receiver.getUserId() == sender.getUserId())
            {
                // Account is linked with same owner. Allow transactions to be made
                return true;
            }

            // Saving does NOT have same owner, must return false
            return false;
        }
        // Account is not saving
        return true;
    }

    public List<Transaction> getTransactionsFromName(Long username) {
        return repositoryTransaction.findAllWithUserId(username);
    }

    public List<Transaction> getTransactionsFromIBAN(String IBAN) {
        return repositoryTransaction.getTransactionsFromIBAN(IBAN);
    }

    public List<Transaction> getTransactionsFromAmount(Double transferAmount) {
        return repositoryTransaction.getTransactionsFromAmount(transferAmount);
    }

    public List<Transaction> FindAllMatches(Long userPerformer, Long transactionId, String IBAN, Double transferAmount, Integer maxNumberOfResults) {
        List<Transaction> myList = new ArrayList<Transaction>();
        User loggedInUser = userApiService.getLoggedInUser();

        if(loggedInUser.getRank() == User.RankEnum.CUSTOMER){
            Long userId = loggedInUser.getId();
            // customer could not see any transactions not related to him
            List<Account> accounts = (List<Account>) accountApiService.getAccountsForUser(userId);
            for (Account account : accounts) {
                if (userPerformer != null) {
                    for (Transaction t : repositoryTransaction.findAllWithUserIdCustomer(userPerformer, account.getIBAN())) {
                        myList.add(t);
                    }
                }
                if (transactionId != null) {
                    List<Transaction> transactions = repositoryTransaction.findAllWithUserIdCustomer(transactionId, account.getIBAN());
                    for (Transaction t : transactions) {
                        myList.add(t);
                    }
                }
                if (IBAN != null) {
                    for (Transaction t : repositoryTransaction.getTransactionsFromIBANCustomer(IBAN, account.getIBAN())) {
                        myList.add(t);
                    }
                }
                if (transferAmount != null) {
                    List<Transaction> transactions = repositoryTransaction.getTransactionsFromAmountCustomer(transferAmount, account.getIBAN());
                    for (Transaction t : transactions) {
                        myList.add(t);
                    }
                }
                if (myList.size() == 0) {
                    List<Transaction> transactions = repositoryTransaction.getAllTransactionsFromCustomer(account.getIBAN());
                    for (Transaction t : transactions) {
                        myList.add(t);
                    }
                }
                if ((maxNumberOfResults != null) && (maxNumberOfResults < myList.size())) {
                    myList = myList.subList(0, maxNumberOfResults);
                }

            }

            return myList;
        }
        else {
            if (userPerformer != null) {
                for (Transaction t : getTransactionsFromName(userPerformer)) {
                    myList.add(t);
                }
            }
            if (transactionId != null) {
                Transaction transaction = repositoryTransaction.findById(transactionId).get();

                if (transaction != null) {
                    myList.add(transaction);
                }
            }
            if (IBAN != null) {
                for (Transaction t : getTransactionsFromIBAN(IBAN)) {
                    myList.add(t);
                }
            }
            if (transferAmount != null) {
                List<Transaction> transactions = getTransactionsFromAmount(transferAmount);
                for (Transaction t : transactions) {
                    myList.add(t);
                }
            }
            if (myList.size() == 0) {
                List<Transaction> transactions = getTransactions();
                for (Transaction t : transactions) {
                    myList.add(t);
                }
            }
            if ((maxNumberOfResults != null) && (maxNumberOfResults < myList.size())) {
                myList = myList.subList(0, maxNumberOfResults);
            }

            return myList;
        }
    }

    public List<Transaction> searchTransactionsUser(UserDetails user) {
        List<Transaction> transactionsForUser = new ArrayList<Transaction>();
        return transactionsForUser;
    }

    public void validateDailyLimit(Account account, Double transferAmount) {
        Double transferAmountToday = 0.00;
        Timestamp startPeriod = getDateWithoutTimeUsingCalendar();
        long oneDay = 1 * 24 * 60 * 60 * 1000; // 84600000 milliseconds in a day
        Timestamp endPeriod = new Timestamp(startPeriod.getTime() + oneDay);


        List<Transaction> transactions = repositoryTransaction.getTransactionsForAccountAndToday(account.getIBAN(), startPeriod, endPeriod);
        for (Transaction transaction : transactions) {
            transferAmountToday += transaction.getTransferAmount();
        }
        if ((transferAmountToday + transferAmount) > account.getDailyLimit()){
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Reached day limit");
        }
    }

    public static Timestamp getDateWithoutTimeUsingCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public Transaction FillInTransactionSpecifics(Transaction transaction) {
        User loggedInUser = userApiService.getLoggedInUser();
        transaction.setUserPerformer(loggedInUser.getId());
        return transaction;
    }
}
