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
import java.util.regex.Pattern;

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

    // get transaction details -> id, (name sender, iban sender, datum, iban receiver, amount)
    public Transaction getTransaction(Long transactionId) {
        return repositoryTransaction.findById(transactionId).get();
    }


    public Boolean makeTransaction(Transaction transaction) {
            /// Use account services to
            /// Find account receiver & sender
            Account receiver = accountApiService.getByIBAN(transaction.getIbanReceiver());
            Account sender = accountApiService.getByIBAN(transaction.getIbanSender());

            // Calculate new balance for account receiver
            Double Rbalance = receiver.getBalance();
            Double Sbalance = sender.getBalance();

            Rbalance += transaction.getTransferAmount();
            Sbalance -= transaction.getTransferAmount();

            receiver.setBalance(Rbalance);
            sender.setBalance(Sbalance);

            // UPDATE accounts in database with included new balance
            accountApiService.updateNewBalanceServiceAccounts(receiver.getBalance(), receiver.getIban());
            accountApiService.updateNewBalanceServiceAccounts(sender.getBalance(), sender.getIban());

            // now the transaction was successful save the transaction
            repositoryTransaction.save(transaction);
            return true;
    }


    public void checkValidTransaction(Transaction body) throws Exception {
        Account accountSender = accountApiService.getByIBAN(body.getIbanSender());
        Account accountReceiver = accountApiService.getByIBAN(body.getIbanReceiver());

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

    public List<Transaction> getTransactionsFromIBAN(String IBAN) {
        return repositoryTransaction.getTransactionsFromIBAN(IBAN);
    }

    public List<Transaction> getTransactionsFromAmount(Double transferAmount) {
        return repositoryTransaction.getTransactionsFromAmount(transferAmount);
    }

    public List<Transaction> FindAllMatches(String userPerformer, String IBAN, Double transferAmount, Integer maxNumberOfResults) {
        List<Transaction> myList = new ArrayList<Transaction>();
        User loggedInUser = userApiService.getLoggedInUser();

        if(loggedInUser.getRank() == User.RankEnum.CUSTOMER){
            Long userId = loggedInUser.getId();
            // customer could not see any transactions not related to him
            //TODO: check if this code is right, get multiple accounts
            List<Account> accounts = new ArrayList<>();
//            List<Account> accounts = (List<Account>) accountApiService.getAccountsForUser(userId);
            for (Account account : accounts) {

                if (IBAN != "" && IBAN != null) {
                    for (Transaction t : repositoryTransaction.getTransactionsFromIBANCustomer(IBAN, account.getIban())) {
                        myList.add(t);
                    }
                }
                if (transferAmount != null) {
                    List<Transaction> transactions = repositoryTransaction.getTransactionsFromAmountCustomer(transferAmount, account.getIban());
                    for (Transaction t : transactions) {
                        myList.add(t);
                    }
                }
                if (myList.size() == 0) {
                    List<Transaction> transactions = repositoryTransaction.getAllTransactionsFromCustomer(account.getIban());
                    for (Transaction t : transactions) {
                        myList.add(t);
                    }
                }

                myList = changeListForView(myList);

                List<Transaction> transactionsMatchingRegex = new ArrayList<>();

                if (userPerformer != "" && userPerformer != null) {
                    for (Transaction transaction : myList) {

                        String regex = ".*"+ userPerformer +".*";
                        if(Pattern.matches(regex, transaction.getNameSender())){
                            transactionsMatchingRegex.add(transaction);
                        }
                    }

                    if(transactionsMatchingRegex.size() != 0){
                        myList = transactionsMatchingRegex;
                    }
                }

                if ((maxNumberOfResults != null) && (maxNumberOfResults < myList.size())) {
                    myList = myList.subList(0, maxNumberOfResults);
                }
            }

            return myList;
        }
        else {
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

            // we want to present the list without transaction id, or user id.
            // instead we add the sender name
            myList = changeListForView(myList);

            List<Transaction> transactionsMatchingRegex = new ArrayList<>();

            if (userPerformer != "" && userPerformer != null) {
                for (Transaction transaction : myList) {

                    String regex = ".*"+ userPerformer +".*";
                    if(Pattern.matches(regex, transaction.getNameSender())){
                        transactionsMatchingRegex.add(transaction);
                    }
                }

                if(transactionsMatchingRegex.size() != 0){
                    myList = transactionsMatchingRegex;
                }
            }



            if ((maxNumberOfResults != null) && (maxNumberOfResults < myList.size())) {
                myList = myList.subList(0, maxNumberOfResults);
            }

            return myList;
        }
    }

    private List<Transaction> changeListForView(List<Transaction> myList) {

        List<Transaction> listAdjustedForView = new ArrayList<>();

        for(Transaction transaction : myList)
        {
            User user = userApiService.getById(transaction.getUserPerformer());
            String sendersName = user.getFirstname() + " " + user.getLastname() + " (" + user.getEmail() + ") ";
            Transaction transactionForView = new Transaction(sendersName, transaction.getIbanSender(), transaction.getIbanReceiver(), transaction.getTransferAmount(), transaction.getTransactionDate());
            listAdjustedForView.add(transactionForView);
        }

        return listAdjustedForView;
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


        List<Transaction> transactions = repositoryTransaction.getTransactionsForAccountAndToday(account.getIban(), startPeriod, endPeriod);
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
