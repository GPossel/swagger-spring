package io.swagger.service;

import io.swagger.dao.RepositoryAccount;
import io.swagger.model.Account;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@NoArgsConstructor
public class AccountApiService {

    @Autowired
    private RepositoryAccount repositoryAccount;

    Account accError = new Account("NLFOUT");

    // post /accounts
    public Account createAccount(Account body) {
        return repositoryAccount.save(body);
    }

    //get /accounts
    public List<Account> getAllAccounts() {
        return (List<Account>) repositoryAccount.findAll();
    }

    public List<Account> getAccountsForUser(Long userId) {
//        if (!UserHasRights(account.getUserId())){
//            throw new Error();
//        }
        return repositoryAccount.getAccountsForUser(userId);
    }

    // Get /accounts/Iban
    public Optional<Account> getAccountByIBAN(String iban) {
        return repositoryAccount.findById(iban);
    }

    // Delete /accounts/Iban
    public void closeAccountFromIBAN(String iban){
        Account account = getAccountByIBAN(iban).get();
//        if (!UserHasRights(account.getUserId())){
//            throw new Error();
//        }
        repositoryAccount.DeleteAccount(iban);
    }

    // Post /accounts/iban/deposit
    public Account depositAccount(String ibanReceiver, double deposit){
//        if (!UserHasRights(account.getUserId())){
//            throw new Error();
//        }
        double balance = repositoryAccount.GetBalance(ibanReceiver) + deposit;
        repositoryAccount.UpdateNewBalance(balance,ibanReceiver);
        return repositoryAccount.findById(ibanReceiver).get();
    }

    // Post /accounts/iban/deposit
    public Account withdrawAccount(String ibanReceiver, double withdraw){
//        if (!UserHasRights(account.getUserId())){
//            throw new Error();
//        }
        double balance = repositoryAccount.GetBalance(ibanReceiver) - withdraw;
        repositoryAccount.UpdateNewBalance(balance,ibanReceiver);
        return repositoryAccount.findById(ibanReceiver).get();
    }

    public void updateNewBalanceServiceAccounts(double NewBalance, String IBAN) {
        repositoryAccount.UpdateNewBalance(NewBalance, IBAN);
    }

    public Double getBalanceOfAccount(String ibanSender) {
        List<Account> allAccounts = (List<Account>) repositoryAccount.findAll();
        for(Account a : allAccounts)
        {
            if(a.getIBAN().equals(ibanSender))
            { return a.getBalance(); }
        }

        return null;
    }

//    public boolean UserHasRights(long userId){
//        if (getLoggedInUser().rank == User.RankEnum.CUSTOMER){
//            if (getLoggedInUser().getRank != userId){
//                return false;
//            }
//        }
//        return true;
//    }
}
