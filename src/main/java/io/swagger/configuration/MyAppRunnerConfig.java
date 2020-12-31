package io.swagger.configuration;

import io.swagger.dao.*;
import io.swagger.model.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

import static io.swagger.model.Account.RankEnum.*;
import static io.swagger.model.Account.StatusEnum.ACTIVE;
import static io.swagger.model.Account.StatusEnum.BLOCKED;

@Component
@Transactional
@ConditionalOnProperty(prefix = "bankshop.autorun", name = "enabled", havingValue = "true", matchIfMissing = true)
public class MyAppRunnerConfig implements ApplicationRunner {

    private RepositoryAccount repositoryAccount;
    private RepositoryTransaction repositoryTransaction;
    private RepositoryUser repositoryUser;
    private PropertyConfig properties;


    public MyAppRunnerConfig(RepositoryAccount accountRepository, RepositoryTransaction repositoryTransaction, RepositoryUser repositoryUser, PropertyConfig properties) {
        this.repositoryAccount = accountRepository;
        this.repositoryTransaction = repositoryTransaction;
        this.repositoryUser = repositoryUser;
        this.properties = properties;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<Account> accounts = new ArrayList<>(
                Arrays.asList(
                        new Account("NL01INHO0000000001", 1000051L, BANK, ACTIVE, 100000.000D, "EUR"),
                        new Account("NL77INHO0123456789", 1000052L, CURRENT, ACTIVE, 1660.00D, "EUR"),
                        new Account("NL22INHO9876543210", 1000053L, SAVING, ACTIVE, 5504.00D, "EUR"),
                        new Account("NL22INHO9999999999", 1000053L, CURRENT, ACTIVE, 904.00D, "EUR"),
                        new Account("NL33INHO3333333333", 1000054L, CURRENT, BLOCKED, 604.00D, "EUR"),
                        new Account("NL11INHO1111111111", 1000055L, CURRENT, ACTIVE, 1700.00D, "EUR")
                )
        );
        accounts.forEach(repositoryAccount::save);
        repositoryAccount.findAll().forEach(System.out::println);

        List<Transaction> transactions = new ArrayList<>(
                Arrays.asList(
                        new Transaction("NL33INHO3333333333", "NL77INHO0123456789", 1000054L, "20-10-1990 12:23:23", 140D),
                        new Transaction("NL77INHO0123456789", "NL22INHO9876543210", 1000052L, "20-10-1990 12:23:23", 10D),
                        new Transaction("NL22INHO9876543210", "NL33INHO3333333333", 1000055L, "20-10-1990 12:23:23", 9D),
                        new Transaction("NL22INHO9999999999", "NL22INHO9876543210", 1000053L, "20-10-1990 12:23:23", 100D),
                        new Transaction("NL22INHO9999999999", "NL22INHO9876543210", 1000053L, "20-10-1990 12:23:23", 100D),
                        new Transaction("NL33INHO3333333333", "NL22INHO9999999999", 1000054L, "20-10-1990 12:23:23", 100D),
                        new Transaction("NL33INHO3333333333", "NL22INHO9999999999", 1000054L, "20-10-1990 12:23:23", 100D),
                        new Transaction("NL33INHO3333333333", "NL22INHO9999999999", 1000054L, "20-10-1990 12:23:23", 100D),
                        new Transaction("NL33INHO3333333333", "NL22INHO9999999999", 1000054L, "20-10-1990 12:23:23", 100D),
                        new Transaction("NL22INHO9876543210", "NL33INHO3333333333", 1000053L, "20-10-1990 12:23:23", 100D),
                        new Transaction("NL11INHO1111111111", "NL22INHO9876543210", 1000055L, "20-10-1990 12:23:23", 90D),
                        new Transaction("NL11INHO1111111111", "NL33INHO3333333333", 1000055L, "20-10-1990 12:23:23", 73D)
                )
        );

        transactions.forEach(repositoryTransaction::save);
        repositoryTransaction.findAll().forEach(System.out::println);

        List<User> users = new ArrayList<>(
                Arrays.asList(
                        new User(1000053L, "Bill", "Nye", "billnye@email.com", "test", "0612345678", "20-10-1990", "20-10-1990 12:23:23", User.RankEnum.CUSTOMER, User.StatusEnum.ACTIVE),
                        new User(1000053L, "Henk", "Anders", "customer@email.com", "test", "0687654321", "20-10-1990","20-10-2019 12:23:23", User.RankEnum.CUSTOMER, User.StatusEnum.ACTIVE),
                        new User(1000054L,"Klaas", "Vaak", "klaasvaak@email.com", "test", "0600112233", "20-10-1990", "20-10-2019 12:23:23", User.RankEnum.CUSTOMER, User.StatusEnum.ACTIVE),
                        new User(1000055L, "Test", "Nye", "test@email.com", "test", "0612345678", "20-10-1990", "20-10-2019 12:23:23", User.RankEnum.EMPLOYEE, User.StatusEnum.ACTIVE),
                        new User(1000056L, "Test", "Nye", "Admin@email.com", "admin", "0612345678", "20-10-1990", "20-10-2019 12:23:23", User.RankEnum.ADMIN, User.StatusEnum.ACTIVE),
                        new User(1000057L,"Blok", "Blokker", "blocked@email.com", "test", "0600112233", "20-10-1990","10-03-2020 12:23:23", User.RankEnum.CUSTOMER, User.StatusEnum.BLOCKED),
                        new User(1000058L, "Test", "Nye", "test1@email.com", "test1", "0612345678", "20-10-1990", "20-10-2019 12:23:23", User.RankEnum.EMPLOYEE, User.StatusEnum.ACTIVE)
                        )
        );

        users.forEach(repositoryUser::save);
        repositoryUser.findAll().forEach(System.out::println);

        System.out.println(repositoryUser.findByFirstname("Test"));
        System.out.println("Application name: " + properties.getApplicationName());
    }
}

