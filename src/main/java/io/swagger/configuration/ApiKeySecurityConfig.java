//package io.swagger.configuration;
//
//import io.swagger.dao.RepositoryApiKey;
//import io.swagger.model.ApiKey;
//import io.swagger.model.Login;
//import io.swagger.model.User;
//import io.swagger.service.UserApiService;
//import lombok.extern.java.Log;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCrypt;
//
//import java.io.UnsupportedEncodingException;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.UUID;
//
//@Configuration
//@EnableWebSecurity
//@Log
//public class ApiKeySecurityConfig extends WebSecurityConfigurerAdapter {
//
//    public final UserApiService userApiService;
//    private RepositoryApiKey repositoryApiKey;
//    @Value("${bankshop.api.token.header-name}")
//    private String headerName;
//
//    public ApiKeySecurityConfig(RepositoryApiKey repository, UserApiService userApiService) {
//        this.repositoryApiKey = repository;
//        this.userApiService = userApiService;
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        ApiKeyAuthFilter filter = new ApiKeyAuthFilter(headerName);
//        filter.setAuthenticationManager(authentication -> {
//            String principle = (String) authentication.getPrincipal();
//
//            if (!repositoryApiKey.findById(principle).isPresent()) {
//                throw new BadCredentialsException("Api Key was not found in the systems");
//            }
//
//            authentication.setAuthenticated(true);
//            return authentication;
//        });
//
//        http
//                .antMatcher("/transactions")
//                //      .antMatcher("/Login")
//                .antMatcher("/users")
//                .csrf().disable() // disable X-site forgery
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // every new request needs auth
//                .and()
//                .addFilter(filter)
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated();
//
//    }
//
//    public ApiKey ValidateUserAndReturnApiKey(Login login) throws Exception {
//        if (!login.getUsername().isEmpty()) {
//            // password is missing
//            User user = userApiService.getUser(login.getUsername());
//            user.setPasswordEncrypt(user.getPassword());
//            if (user != null) {
//                if (BCrypt.checkpw(login.getPassword(), user.getPassword())) {
//                    // Delete all existing apikeys for user
//                    repositoryApiKey.DeleteOldApiKeys(user.getId());
//                    // Generate new apikey
//                    ApiKey apiKey = CreateApiKey(user);
//
//                    while(ApiKeyAlreadyExists(apiKey))
//                    {
//                        apiKey = CreateApiKey(user);
//                    }
//
//                    repositoryApiKey.save(apiKey);
//                    ApiKey userApiKey = repositoryApiKey.findApiKeyByUser(user.getId());
//
//                    if (apiKey != null) {
//                        return userApiKey;
//                    }
//                    throw new Exception("No ApiKey made for user!");
//                }
//                throw new Exception("Bad password!");
//            }
//            throw new Exception("No user found with username!");
//        }
//        throw new Exception("Username is empty!");
//    }
//
//    private boolean ApiKeyAlreadyExists(ApiKey apiKey) {
//        ApiKey duplicateApiKey = repositoryApiKey.findApiKey(apiKey.getApiKey());
//        if(duplicateApiKey == null) {
//            return false;
//        }
//        return true;
//    }
//
//
//    private ApiKey CreateApiKey(User user) throws NoSuchAlgorithmException, UnsupportedEncodingException {
//        UUID uuid = UUID.randomUUID();
//        MessageDigest salt = MessageDigest.getInstance("SHA-256");
//        salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
//        String digest = bytesToHex(salt.digest());
//        //token will expire after 60min from now
//        ApiKey apiKey = new ApiKey(digest, user.getId(), LocalDateTime.now(), LocalDateTime.now().plusMinutes(60));
//        return apiKey;
//    }
//    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
//
//    private String bytesToHex(byte[] bytes) {
//        char[] hexChars = new char[bytes.length * 2];
//        for (int j = 0; j < bytes.length; j++) {
//            int v = bytes[j] & 0xFF;
//            hexChars[j * 2] = hexArray[v >>> 4];
//            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
//        }
//        return new String(hexChars);
//    }
//}
