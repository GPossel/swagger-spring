package io.swagger.dao;

import io.swagger.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface RepositoryTransaction extends CrudRepository<Transaction, Long> {

    @Query("select t from Transaction t where t.userPerformer = :userPerformer")
    List<Transaction> findAllWithUserId(@Param("userPerformer") String userPerformer);

    @Query("select t from Transaction t where t.ibanSender =:IBAN OR t.ibanReciever =:IBAN")
    List<Transaction> getTransactionsFromIBAN(@Param("IBAN") String IBAN);

    @Query("select t from Transaction t where t.transferAmount =:transferAmount")
    List<Transaction> getTransactionsFromAmount(@Param("transferAmount") Double transferAmount);


    // Customer only
    @Query("select t from Transaction t where t.ibanSender =:IBAN OR t.ibanReciever =:IBAN and t.ibanSender = :account OR t.ibanReciever = :account")
    List<Transaction> getTransactionsFromIBANCustomer(@Param("IBAN") String IBAN, @Param("account") String account);

    @Query("select t from Transaction t where t.ibanSender =:IBAN OR t.ibanReciever =:IBAN")
    List<Transaction> getAllTransactionsFromCustomer(@Param("IBAN") String IBAN);

    @Query("select t from Transaction t where t.transferAmount =:transferAmount and t.ibanSender = :account OR t.ibanReciever = :account")
    List<Transaction> getTransactionsFromAmountCustomer(@Param("transferAmount") Double transferAmount, @Param("account") String account);

    @Query(value = "select t  from Transaction t where t.ibanSender = :iban and t.transactionDate between :startPeriod and :endPeriod")
    List<Transaction> getTransactionsForAccountAndToday(@Param("iban") String iban, @Param("startPeriod")Timestamp startPeriod, @Param("endPeriod")Timestamp endPeriod);
}