package io.swagger.service;

import io.swagger.dao.RepositoryAccount;
import io.swagger.model.*;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@NoArgsConstructor
public class AccountApiService {

    @Autowired
    private RepositoryAccount repositoryAccount;

    @Autowired
    private  UserApiService userApiService;

    User loggedInUser;

    public AccountResponse create(AccountRequest body) {
        if (body.getRank() == Account.RankEnum.BANK){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't create an account with rank: BANK");
        }
        Account account = repositoryAccount.save(new Account(body));
        return new AccountResponse(account);
    }

    public Iterable<AccountResponse> getAll() {
        if (loggedInUser == null) {
            loggedInUser = userApiService.getLoggedInUser();
            if (loggedInUser.getRank() != User.RankEnum.EMPLOYEE){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        }
        Iterable<Account> accounts = repositoryAccount.getAccountsForEmployee(Account.RankEnum.BANK); //param !rank
        return convertListAccountToResponse(accounts);
    }

    public Account getByIBAN(String iban) {
        Optional<Account> optionalAccount = repositoryAccount.findById(iban);
        if (!optionalAccount.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return optionalAccount.get();
    }

    public AccountResponse getByIbanWithAuth(String iban) {
        Account account = getByIBAN(iban);
        if (!UserHasRights(account.getUserId())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return new AccountResponse(account);
    }

    public void delete(String iban)  {
        Account account = getByIBAN(iban);
        account.setStatus(Account.StatusEnum.DELETED);

        Integer i = repositoryAccount.update(account.getIban(), account);
        if (i == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    //TODO CHECK OP NULL IN MODEL
    public Account update(String iban, Account body) {
        Integer i = repositoryAccount.update(iban, body);
        if (i == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
        return getByIBAN(iban);
    }

    public Iterable<Account> getAccountsForUser(Long userId) {
        if (!UserHasRights(userId)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return repositoryAccount.getAccountsForEmployee(Account.RankEnum.BANK); //param !rank
    }

    public Iterable<AccountResponse> responseGetAccountsForUser(Long userId) {
        Iterable<Account> accounts = getAccountsForUser(userId);
        return convertListAccountToResponse(accounts);
    }

    public ATMResponse deposit(ATMRequest body) {
        Account account = validateATMRequest(body.getIBAN(), body.getTransferAmount());

        Double oldBalance = account.getBalance();
        account.setBalance(account.getBalance() + body.getTransferAmount());

        account = update(account.getIban(), account);
        return new ATMResponse(account, oldBalance);
    }

    public ATMResponse withdraw(ATMRequest body) {
        Account account = validateATMRequest(body.getIBAN(), body.getTransferAmount());

        Double oldBalance = account.getBalance();
        account.setBalance(account.getBalance() - body.getTransferAmount());

        account = update(account.getIban(), account);
        return new ATMResponse(account, oldBalance) ;
    }

    private Account validateATMRequest(String iban, double amount) {
        Account account = getByIBAN(iban);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User)authentication.getPrincipal();

        // validate this account belongs to the logged in user,
        if(!account.getUserId().equals(loggedInUser.getId())) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Account: " + iban + " is not account of: " + loggedInUser.getEmail());
        }
        if(amount < 0) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Amount was negative. Please enter positive amounts only > 0.");
        }

        return account;
    }

    public void updateNewBalanceServiceAccounts(double NewBalance, String iban) {
        Account account = getByIBAN(iban);
        account.setBalance(NewBalance);
        repositoryAccount.update(iban, account);
    }

    public Iterable<AccountResponse> convertListAccountToResponse(Iterable<Account> accounts){
        List<AccountResponse> accountResponseList = new ArrayList<AccountResponse>();
        accounts.forEach(account -> {
            accountResponseList.add(new AccountResponse(account));
        });
        return accountResponseList;
    }

    public boolean UserHasRights(Long userId){
        if (loggedInUser == null) {
            loggedInUser = userApiService.getLoggedInUser();
        }

        if (loggedInUser.getRank() != User.RankEnum.EMPLOYEE){
            if (!loggedInUser.getId().equals(userId)){
                return false;
            }
        }
        return true;
    }
}
