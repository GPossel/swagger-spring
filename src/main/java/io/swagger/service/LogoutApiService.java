package io.swagger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogoutApiService {

    public void logoutUser(String username)
    {
        //     repositoryApiKey.deleteByUsername(username);
        System.out.println("succesfull logout");
    }
}
