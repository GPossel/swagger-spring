package io.swagger.service;

import io.swagger.dao.RepositoryAccount;
import io.swagger.model.Account;
import io.swagger.model.AccountRequest;
import io.swagger.model.User;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Service
@NoArgsConstructor
public class AccountApiService {

    @Autowired
    private RepositoryAccount repositoryAccount;

    @Autowired
    private  UserApiService userApiService;

    User loggedInUser;

    public Account create(AccountRequest body) {
        if (body.getRank() == Account.RankEnum.BANK){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't create an account with rank: BANK");
        }
        return repositoryAccount.save(new Account(body));
    }

    public Iterable<Account> getAll() {
        if (this.loggedInUser == null) {
            this.loggedInUser = userApiService.getLoggedInUser();
            if (this.loggedInUser.getRank() != User.RankEnum.EMPLOYEE){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        }

        return repositoryAccount.getAccountsForEmployee(Account.RankEnum.BANK); //param !rank
    }

    public Account getByIBAN(String iban) {
        Optional<Account> optionalAccount = repositoryAccount.findById(iban);
        if (!optionalAccount.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return optionalAccount.get();
    }

    public Account getByIbanWithAuth(String iban) {
        Account account = this.getByIBAN(iban);
        if (!UserHasRights(account.getUserId())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return account;
    }

    public void delete(String iban)  {
        Account account = this.getByIBAN(iban);

        account.setStatus(Account.StatusEnum.DELETED);
        Integer i = repositoryAccount.Update(account.getIban(), account);
        if (i == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public Iterable<Account> getAccountsForUser(Long userId) {
        if (!UserHasRights(userId)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return repositoryAccount.getAccountsForUser(userId);
    }

    public Account deposit(String ibanReceiver, double deposit) {
        Account account = this.getByIBAN(ibanReceiver);

        account.setBalance(account.getBalance() + deposit);

        repositoryAccount.Update(ibanReceiver, account);
        return repositoryAccount.findById(ibanReceiver).get();
    }

    public Account withdraw(String ibanReceiver, double withdraw) {
        Account account = this.getByIBAN(ibanReceiver);
        account.setBalance(account.getBalance() - withdraw);

        repositoryAccount.Update(ibanReceiver, account);
        return repositoryAccount.findById(ibanReceiver).get();
    }

    public void updateNewBalanceServiceAccounts(double NewBalance, String IBAN) {
        Account account = this.getByIBAN(IBAN);

        account.setBalance(NewBalance);

        repositoryAccount.Update(IBAN, account);
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
