package io.swagger.service;

import io.cucumber.java.Before;
import io.swagger.model.Transaction;
import io.swagger.model.TransactionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    TransactionApiService transactionApiService;

    @Before
    public void setUp() {}

    @Test
    public void ListMustNotBeNull() throws Exception
    {
        List<TransactionResponse> transactions = transactionApiService.FindAllMatches("Bill", "", null, null);
        assertNotNull(transactions);
    }
}
