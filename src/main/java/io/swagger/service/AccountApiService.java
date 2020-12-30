package io.swagger.service;

import io.swagger.dao.RepositoryAccount;
import io.swagger.model.*;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    public AccountResponse deposit(String ibanReceiver, double deposit) {
        Account account = this.getByIBAN(ibanReceiver);
        account.setBalance(account.getBalance() + deposit);
        return this.update(ibanReceiver, account);
    }

    public AccountResponse withdraw(String ibanReceiver, double withdraw) {
        Account account = this.getByIBAN(ibanReceiver);
        account.setBalance(account.getBalance() - withdraw);
        return this.update(ibanReceiver, account);
    }

    public void updateNewBalanceServiceAccounts(double NewBalance, String IBAN) {
        Account account = this.getByIBAN(IBAN);
        account.setBalance(NewBalance);

        repositoryAccount.update(IBAN, account);
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
