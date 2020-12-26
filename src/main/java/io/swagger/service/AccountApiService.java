package io.swagger.service;

import io.swagger.dao.RepositoryAccount;
import io.swagger.model.Account;
import io.swagger.model.AccountUser;
import io.swagger.model.User;
import javassist.NotFoundException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.security.KeyException;
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

    // post /accounts
    public Account createAccount(Account body) {
        return repositoryAccount.save(body);
    }

    //get /accounts
    public Iterable<Account> getAllAccounts() {
        return repositoryAccount.findAll();
    }

    public Iterable<Account> getAccountsForUser(Long userId) {
        if (!UserHasRights(userId)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        User user = userApiService.getById(userId);
        if (user.getRank() == User.RankEnum.EMPLOYEE){
            return repositoryAccount.findAll();
        }

        return repositoryAccount.getAccountsForUser(userId);
    }

    // Get /accounts/Iban
    public Account getAccountByIBAN(String iban) {
        Optional<Account> user = repositoryAccount.findById(iban);

        if (!user.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return user.get();
    }


    public void deleteAccount(String iban)  {
        Account account = this.getAccountByIBAN(iban);
        if (!UserHasRights(account.getUserId())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        account.setStatus(Account.StatusEnum.DELETED);
        Integer i = repositoryAccount.Update(account.getIBAN(), account);
        if (i == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    // Post /accounts/iban/deposit
    public Account depositAccount(String ibanReceiver, double deposit) {
        Account account = this.getAccountByIBAN(ibanReceiver);
        if (!UserHasRights(account.getUserId())){
            throw new Error();
        }

        account.setBalance(account.getBalance() + deposit);

        repositoryAccount.Update(ibanReceiver, account);
        return repositoryAccount.findById(ibanReceiver).get();
    }

    // Post /accounts/iban/deposit
    public Account withdrawAccount(String ibanReceiver, double withdraw) {
        Account account = this.getAccountByIBAN(ibanReceiver);
        if (!UserHasRights(account.getUserId())){
            throw new Error();
        }
        account.setBalance(account.getBalance() - withdraw);

        repositoryAccount.Update(ibanReceiver, account);
        return repositoryAccount.findById(ibanReceiver).get();
    }

    public void updateNewBalanceServiceAccounts(double NewBalance, String IBAN) {
        Account account = this.getAccountByIBAN(IBAN);

        account.setBalance(NewBalance);

        repositoryAccount.Update(IBAN, account);
    }

    public boolean UserHasRights(Long userId){
        User loggedInUser = userApiService.getLoggedInUser();
        if (loggedInUser.getRank() != User.RankEnum.EMPLOYEE){
            if (loggedInUser.getId() != userId){
                return false;
            }
        }
        return true;
    }
}
