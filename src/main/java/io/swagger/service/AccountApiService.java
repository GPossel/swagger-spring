package io.swagger.service;

import io.swagger.dao.RepositoryAccount;
import io.swagger.model.Account;
import io.swagger.model.AccountRequest;
import io.swagger.model.AccountResponse;
import io.swagger.model.User;
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
        if (this.loggedInUser == null) {
            this.loggedInUser = userApiService.getLoggedInUser();
            if (this.loggedInUser.getRank() != User.RankEnum.EMPLOYEE){
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
        Account account = this.getByIBAN(iban);
        if (!UserHasRights(account.getUserId())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return new AccountResponse(account);
    }

    public void delete(String iban)  {
        Account account = this.getByIBAN(iban);

        account.setStatus(Account.StatusEnum.DELETED);
        Integer i = repositoryAccount.update(account.getIban(), account);
        if (i == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public AccountResponse update(String iban, Account body) {
        Integer i = repositoryAccount.update(iban, body);
        if (i == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
        return new AccountResponse(this.getByIBAN(iban));
    }

    public Iterable<AccountResponse> getAccountsForUser(Long userId) {
        if (!UserHasRights(userId)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Iterable<Account> accounts = repositoryAccount.getAccountsForEmployee(Account.RankEnum.BANK); //param !rank
        return convertListAccountToResponse(accounts);
    }

    // Post /accounts/{iban}/deposit
    public AccountResponse deposit(String ibanReceiver, double deposit) {
        Account account = validateATMRequest(ibanReceiver, deposit);
        account.setBalance(account.getBalance() + deposit);
        return this.update(ibanReceiver, account);

        }

    // Post /accounts/{iban}/deposit
    public AccountResponse withdraw(String ibanReceiver, double withdraw) {
        Account account = validateATMRequest(ibanReceiver, withdraw);
        account.setBalance(account.getBalance() - withdraw);
        return this.update(ibanReceiver, account);
    }

    private Account validateATMRequest(String iban, double amount) {
        Account account = this.getByIBAN(iban);

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
        Account account = this.getByIBAN(iban);
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
        if (this.loggedInUser == null) {
            this.loggedInUser = userApiService.getLoggedInUser();
        }

        if (this.loggedInUser.getRank() != User.RankEnum.EMPLOYEE){
            if (!this.loggedInUser.getId().equals(userId)){
                return false;
            }
        }
        return true;
    }
}
