package io.swagger.dao;

import io.swagger.model.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepositoryAccount extends CrudRepository<Account, String> {


    @Query("select a from Account a where a.userId =:userId")
    Iterable<Account> getAccountsForUser(@Param("userId") Long userId);

    @Query(value = "select a from Account a where not(a.rank = :rank)")
    Iterable<Account> getAccountsForEmployee(@Param("rank")Account.RankEnum rank);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Account a SET a = :ACCOUNT WHERE a.IBAN = :IBAN")
    Integer Update(@Param("IBAN") String IBAN, @Param("ACCOUNT") Account ACCOUNT);
}