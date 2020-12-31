package io.swagger.service;

import io.cucumber.java.Before;
import io.swagger.model.User;
import io.swagger.model.UserRequest;
import io.swagger.model.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserApiService userApiService;

    @Before
    public void setUp() {
    }

    @Test
    public void ListMustNotBeNull() throws Exception {
        Iterable<UserResponse> users = userApiService.getAll(new UserRequest());
        assertNotNull(users);
    }
}
