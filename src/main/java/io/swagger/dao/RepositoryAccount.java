package io.swagger.dao;

import io.swagger.model.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RepositoryAccount extends CrudRepository<Account, String> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE Account a SET a.balance = :balance WHERE a.IBAN = :IBAN")
    void UpdateNewBalance(@Param("balance") double newBalance, @Param("IBAN") String IBAN);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Account a SET a = :ACCOUNT WHERE a.IBAN = :IBAN")
    Integer Update(@Param("IBAN") String IBAN, @Param("ACCOUNT") Account ACCOUNT);

    @Query(value = "select a.balance from Account a where a.IBAN =:IBAN", nativeQuery = true)
    double GetBalance(@Param("IBAN") String IBAN);

    @Transactional
    @Modifying
    @Query("select a from Account a where a.userId =:userId")
    Iterable<Account> getAccountsForUser(@Param("userId") Long userId);
}