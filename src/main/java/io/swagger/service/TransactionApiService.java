package io.swagger.service;

import io.swagger.dao.RepositoryTransaction;
import io.swagger.model.*;
import org.hibernate.validator.internal.constraintvalidators.bv.number.bound.MaxValidatorForNumber;
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
import java.util.stream.Collectors;

@Service
public class TransactionApiService {

    @Autowired
    private RepositoryTransaction repositoryTransaction;

    @Autowired
    private AccountApiService accountApiService;

    @Autowired
    private  UserApiService userApiService;

    private User loggedInUser;

    public TransactionApiService() {
    }

    public Transaction create(TransactionRequest body){
        if (loggedInUser == null){
            loggedInUser = userApiService.getLoggedInUser();
        }

        Account accountSender = accountApiService.getByIBAN(body.getIbanSender());
        Account accountReceiver = accountApiService.getByIBAN(body.getIbanReceiver());

        Transaction transaction = new Transaction(body, loggedInUser.getId());
        checkValidTransaction(transaction, accountSender, accountReceiver);

        accountSender.setBalance(accountSender.getBalance() - transaction.getTransferAmount());
        accountReceiver.setBalance(accountReceiver.getBalance() + transaction.getTransferAmount());

        accountApiService.update(accountSender.getIban(), accountSender);
        accountApiService.update(accountReceiver.getIban(), accountReceiver);

        return repositoryTransaction.save(transaction);
    }

    public List<TransactionResponse> getAllTransactionsResponses() {
        List<Transaction> transactions = (List<Transaction>) repositoryTransaction.findAll();
        return changeListForView(transactions);
    }

    public Iterable<Transaction> getAll()
    {
        return repositoryTransaction.findAll();
    }

    public TransactionResponse getById(Long id) {
        Optional<Transaction> optionalTransaction = repositoryTransaction.findById(id);
        if (!optionalTransaction.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        TransactionResponse transactionResponse = convertTransactionToResponse(optionalTransaction.get());
        return transactionResponse;
    }

    public List<Transaction> getTransactionsFromIBAN(String IBAN) {
        return repositoryTransaction.getTransactionsFromIBAN(IBAN);
    }

    public List<TransactionResponse> getTransactionsForAccountByIBAN(String iban) {
        Account account = accountApiService.getByIBAN(iban);
        if (!UserHasRights(account.getUserId())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        List<Transaction> transactions = repositoryTransaction.getTransactionsFromIBAN(iban);
        return changeListForView(transactions);
    }

    public List<Transaction> getTransactionsFromAmount(Double transferAmount) {
        return repositoryTransaction.getTransactionsFromAmount(transferAmount);
    }

    public List<TransactionResponse> FindAllMatches(String nameUser, String IBAN, Double transferAmount, Integer maxNumberOfResults) {
            List<Transaction> transactionList;
            User loggedInUser = userApiService.getLoggedInUser();

            if(loggedInUser.getRank().equals(User.RankEnum.CUSTOMER)){
                transactionList = FindMatchesCustomer(nameUser, IBAN, transferAmount);
            }
            else {
                transactionList = FindMatches(nameUser, IBAN, transferAmount);
            }

            List<TransactionResponse> transactionResponses = changeListForView(transactionList);

            if (nameUser != "" && nameUser != null) {
                transactionResponses = FindByUserName(nameUser, transactionResponses);
            }

            if((maxNumberOfResults != null) && (maxNumberOfResults < transactionList.size())) {
                transactionResponses = transactionResponses.subList(0, maxNumberOfResults);
            }

            return  transactionResponses;
        }

    private List<TransactionResponse> FindByUserName(String nameUser, List<TransactionResponse> transactionResponses) {
        List<TransactionResponse> transactionsMatchingRegex = new ArrayList<>();

        for (TransactionResponse transaction : transactionResponses) {

            String regex = ".*"+ nameUser +".*";
            if(Pattern.matches(regex, transaction.getNameSender())){
                transactionsMatchingRegex.add(transaction);
            }
            if(Pattern.matches(regex, transaction.getNameReciever())){
                transactionsMatchingRegex.add(transaction);
            }
            if(Pattern.matches(regex, transaction.getUserSender())){
                transactionsMatchingRegex.add(transaction);
            }
        }

        // remove duplicates
        List<TransactionResponse> transactionsMatchingRegexDistinct = transactionsMatchingRegex.stream()
                .distinct()
                .collect(Collectors.toList());

        return transactionsMatchingRegexDistinct;
    }

    private List<Transaction> FindMatches(String userPerformer, String IBAN, Double transferAmount){

    List<Transaction> myList = new ArrayList<Transaction>();
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
            Iterable<Transaction> transactions = getAll();
            for (Transaction t : transactions) {
                myList.add(t);
            }
        }

        return myList;
    }

    private List<Transaction> FindMatchesCustomer(String userPerformer, String IBAN, Double transferAmount) {

        List<Transaction> myList = new ArrayList<Transaction>();
        Long userId = loggedInUser.getId();

        Iterable<Account> accounts =  accountApiService.getAccountsForUser(userId);
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
        }

        return myList;
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

    public void checkValidTransaction(Transaction transaction, Account accountSender, Account accountReceiver) {

        validateDailyLimit(accountSender, transaction.getTransferAmount());

        // validate this account belongs to the logged in user, or is employee
        if(!UserHasRights(accountSender.getUserId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Transactions from: " + transaction.getIbanSender() + " is not account of: " + loggedInUser.getEmail());
        }

        // validate the account is still active and the receivers account is active
        if (accountSender.getStatus() != Account.StatusEnum.ACTIVE || accountReceiver.getStatus() != Account.StatusEnum.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "One of the accounts is not Active, it might blocked or deleted");
        }

        if (transaction.getTransferAmount() > accountSender.getTransferLimit()) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Transfer amount is more than the limit");
        }

        //     Account has not enough money
        if (transaction.getTransferAmount() > (accountSender.getBalance() - 500)) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Sender account has not enough money");
        }

        // Checks if accounts are from type saving and if so, has same owner then proceed transaction,
        if (!validateAccountForAccountByType(accountSender, accountReceiver)) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Transactions from or to a savings account must be from the owner");
        }
    }

    // Checks if account is a saving and if so is this account from same owner then allowed to proceed,
    private Boolean validateAccountForAccountByType(Account accountSender, Account accountReciever) {

        // Account is liked with same owner. Allow transactions to be made
        if(accountReciever.getUserId() != accountSender.getUserId())
        {
            if((accountSender.getRank() == Account.RankEnum.SAVING) || (accountReciever.getRank() == Account.RankEnum.SAVING))
            {
                return false;
            }
        }
        return true;
    }

    private List<TransactionResponse> changeListForView(List<Transaction> transactions) {

        List<TransactionResponse> responseList = new ArrayList<>();

        for(Transaction transaction : transactions)
        {
            TransactionResponse transactionResponse = convertTransactionToResponse(transaction);
            responseList.add(transactionResponse);
        }

        return responseList;
    }

    private TransactionResponse convertTransactionToResponse(Transaction transaction) {

            User user = userApiService.getById(transaction.getUserPerformer());
            User userSender = userApiService.getById(accountApiService.getByIBAN(transaction.getIbanSender()).getUserId());
            User userReciever = userApiService.getById(accountApiService.getByIBAN(transaction.getIbanReceiver()).getUserId());

            String userName = user.getFirstname() + " " + user.getLastname() + " (" + user.getEmail() + ") ";
            String nameSender = userSender.getFirstname() + " " + userSender.getLastname() + " (" + user.getEmail() + ") ";
            String nameReciever = userReciever.getFirstname() + " " + userReciever.getLastname() + " (" + user.getEmail() + ") ";

            TransactionResponse transactionResponse = new TransactionResponse(transaction, userName, nameSender, nameReciever);

            return transactionResponse;
    }

    public static Timestamp getDateWithoutTimeUsingCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public User getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User)authentication.getPrincipal();
    }

    public boolean UserHasRights(Long userId){

        loggedInUser = getLoggedInUser();

        if (!loggedInUser.getRank().equals(User.RankEnum.EMPLOYEE)){
            if (!this.loggedInUser.getId().equals(userId)){
                return false;
            }
        }
        return true;
    }

}
