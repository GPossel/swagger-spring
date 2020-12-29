package io.swagger.configuration;

import io.swagger.dao.*;
import io.swagger.model.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
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
                        new Account(1000051L, "NL01INHO0000000001", BANK, ACTIVE, 100000.000D, "EUR"),
                        new Account(1000052L, "NL77INHO0123456789", CURRENT, ACTIVE, 1660.00D, "EUR"),
                        new Account(1000053L, "NL22INHO9876543210", SAVING, ACTIVE, 5504.00D, "EUR"),
                        new Account(1000053L, "NL22INHO9999999999", CURRENT, ACTIVE, 904.00D, "EUR"),
                        new Account(1000054L, "NL33INHO3333333333", CURRENT, BLOCKED, 604.00D, "EUR"),
                        new Account(1000055L, "NL11INHO1111111111", CURRENT, ACTIVE, 1700.00D, "EUR")
                )
        );
        accounts.forEach(repositoryAccount::save);
        repositoryAccount.findAll().forEach(System.out::println);

        List<Transaction> transactions = new ArrayList<>(
                Arrays.asList(
                        new Transaction("NL33INHO3333333333", "NL77INHO0123456789", 1000054L, 140D),
                        new Transaction("NL77INHO0123456789", "NL22INHO9876543210", 1000052L, 10D),
                        new Transaction("NL22INHO9876543210", "NL33INHO3333333333", 1000055L, 9D),
                        new Transaction("NL22INHO9999999999", "NL22INHO9876543210", 1000053L, 100D),
                        new Transaction("NL22INHO9999999999", "NL22INHO9876543210", 1000053L, 100D),
                        new Transaction("NL33INHO3333333333", "NL22INHO9999999999", 1000054L, 100D),
                        new Transaction("NL33INHO3333333333", "NL22INHO9999999999", 1000054L, 100D),
                        new Transaction("NL33INHO3333333333", "NL22INHO9999999999", 1000054L, 100D),
                        new Transaction("NL33INHO3333333333", "NL22INHO9999999999", 1000054L, 100D),
                        new Transaction("NL22INHO9876543210", "NL33INHO3333333333", 1000053L, 100D),
                        new Transaction("NL11INHO1111111111", "NL22INHO9876543210", 1000055L, 90D),
                        new Transaction("NL11INHO1111111111", "NL33INHO3333333333", 1000055L, 73D)
                )
        );

        transactions.forEach(repositoryTransaction::save);
        repositoryTransaction.findAll().forEach(System.out::println);

        List<User> users =
                Arrays.asList(
                        new User(1000052L, "Bill", "Nye", "billnye@email.com", "test", "0612345678", "1990-11-20", "20-10-2019", User.RankEnum.CUSTOMER, User.StatusEnum.ACTIVE),
                        new User(1000053L, "Henk", "Anders", "customer@email.com", "test", "0687654321", "1994-2-23", "05-02-2020", User.RankEnum.CUSTOMER, User.StatusEnum.ACTIVE),
                        new User(1000054L,"Klaas", "Vaak", "klaasvaak@email.com", "test", "0600112233", "2000-1-21", "10-03-2020", User.RankEnum.CUSTOMER, User.StatusEnum.ACTIVE));

        users.forEach(repositoryUser::save);
        repositoryUser.findAll().forEach(System.out::println);

        repositoryUser.save(new User(1000055L, "Test", "Nye", "test@email.com", "test", "0612345678", "1990-11-20", "20-10-2019", User.RankEnum.EMPLOYEE, User.StatusEnum.ACTIVE));
        repositoryUser.save(new User(1000058L, "Test", "Nye", "test1@email.com", "test1", "0612345678", "1990-11-20", "20-10-2019", User.RankEnum.EMPLOYEE, User.StatusEnum.ACTIVE));
        repositoryUser.save(new User(1000056L, "Test", "Nye", "Admin@email.com", "admin", "0612345678", "1990-11-20", "20-10-2019", User.RankEnum.ADMIN, User.StatusEnum.ACTIVE));
        repositoryUser.save(new User(1000057L,"Blok", "Blokker", "blocked@email.com", "test", "0600112233", "2000-1-21", "10-03-2020", User.RankEnum.CUSTOMER, User.StatusEnum.BLOCKED));

        System.out.println(repositoryUser.findByFirstname("Test"));
        System.out.println("Application name: " + properties.getApplicationName());
    }
}

