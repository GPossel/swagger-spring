package io.swagger.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserModelTest {

    private User user;

    @BeforeEach
    public void Setup() {
        this.user = new User(000000L, "Sander", "Boeree", "625874@student.inholland.nl", "Welkom123",
                "261G599A2C392", "23-04-2000", "20-06-2000 12:23:12", User.RankEnum.ADMIN, User.StatusEnum.ACTIVE);
    }

    @Test
    public void createUserShouldNotBeNull() {
        assertNotNull(user);
    }

}