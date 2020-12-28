package io.swagger.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.model.ATMrequest;
import io.swagger.model.Account;
import io.swagger.model.User;
import io.swagger.service.AccountApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-06-02T09:24:14.507Z[GMT]")
@Controller

public class AccountsApiController implements AccountsApi {

    private static final Logger log = LoggerFactory.getLogger(AccountsApiController.class);
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;

    @Autowired
    private AccountApiService accountApiService;

    @org.springframework.beans.factory.annotation.Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @PreAuthorize("hasAuthority('Employee') or hasAuthority('Admin')")
    public ResponseEntity<Account> createAccount(@ApiParam(value = "created accounts", required = true) @Valid @RequestBody Account body
    ) {
        System.out.println(1);
        System.out.println(body);
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-type");

        if (accept != null && content.contains("application/json")) {
            try {
                if (body != null) {
                    return new ResponseEntity<Account>(objectMapper.readValue(objectMapper.writeValueAsString(accountApiService.createAccount(body)), Account.class), HttpStatus.CREATED);
                }
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Account>(HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (Exception e) {
                ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                return responseEntity;
            }
            }
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<Account>(HttpStatus.NOT_IMPLEMENTED);
    }

//    @PreAuthorize("hasAuthority('Employee') or hasAuthority('Admin')")
    public ResponseEntity<List<Account>> getAccounts(){
        String accept = request.getHeader("Accept");
            if (accept != null) {
                try {
                    Iterable<Account> accounts = accountApiService.getAllAccounts();
                    return new ResponseEntity<List<Account>>(objectMapper.readValue(objectMapper.writeValueAsString(accounts), List.class), HttpStatus.OK);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                    return responseEntity;
                }
            }
        return new ResponseEntity<List<Account>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Account> getAccountByIban(@ApiParam(value= "", required = true) @PathVariable("iban") String iban){
        String accept = request.getHeader("Accept");

            if (accept != null) {
                try {
                    Account account = accountApiService.getAccountByIbanWithAuth(iban);
                    return new ResponseEntity<Account>(objectMapper.readValue(objectMapper.writeValueAsString(account), Account.class), HttpStatus.OK);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                    return responseEntity;
                }
            }
        return new ResponseEntity<Account>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*Delete User*/
    public ResponseEntity<Void> deleteAccount(@ApiParam(value = "The iban that needs to be deleted", required = true) @PathVariable("iban") String iban
    ) {
        String accept = request.getHeader("Accept");

        if (accept != null) {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User loggedInUser = (User)authentication.getPrincipal();
                accountApiService.deleteAccount(iban);
                ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.OK).body((JsonNode) objectMapper.createObjectNode().put("message", "Deleted Successfully!"));
                return responseEntity;
            } catch (Exception e) {
                ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                return responseEntity;
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('Customer')")
    public ResponseEntity<List<Account>> getAccountsForUser(@ApiParam(value= "", required = true) @PathVariable("userid") Long userId){
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-Type");
        if (accept != null && content.contains("application/json")) {
                try {
                    Iterable<Account> accounts = accountApiService.getAccountsForUser(userId);
                    return new ResponseEntity<List<Account>>(objectMapper.readValue(objectMapper.writeValueAsString(accounts), List.class), HttpStatus.OK);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                    return responseEntity;
                }
            }
        return new ResponseEntity<List<Account>>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity withdraw(@ApiParam(value = "created withdraw", required = true) @Valid @RequestBody ATMrequest body)
    {
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-Type");
        if (accept != null && content.contains("application/json")) {
            try {
                Double oldBalance = 0d;
                Double newBalance = 0d;

                if(body.getPincode().equals(1234) && !(body.getTransferAmount() < 0)){
   //                 oldBalance = accountApiService.getAccountByIbanWithAuth(body.getIBAN()).getBalance();
                    Account account = accountApiService.getAccountByIbanWithAuth(body.getIBAN());
                    oldBalance = account.getBalance();
                    account = accountApiService.withdrawAccount(body.getIBAN(), body.getTransferAmount());
                    newBalance = account.getBalance();
                }

                ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.OK).body((JsonNode) objectMapper.createObjectNode().put("message", "Old balance was: "
                + oldBalance.toString() + "\n New balance: " + newBalance.toString() + "\n withdraw: -" + body.getTransferAmount().toString()));
                return responseEntity;

            } catch (Exception e) {
                ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                return responseEntity;
            }
        }
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body((JsonNode) objectMapper.createObjectNode().put("message", "Wrong headers"));
        return responseEntity;
    }

    public ResponseEntity deposit(@ApiParam(value = "created deposit", required = true) @Valid @RequestBody ATMrequest body)
    {
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-Type");
        if (accept != null && content.contains("application/json")) {
            try {
                Double oldBalance = 0d;
                Double newBalance = 0d;

                if(body.getPincode().equals(1234) && !(body.getTransferAmount() < 0)){
          //          oldBalance = accountApiService.getAccountByIbanWithAuth(body.getIBAN()).getBalance();
                    Account account = accountApiService.getAccountByIbanWithAuth(body.getIBAN());
                    oldBalance = account.getBalance();
                    account = accountApiService.depositAccount(body.getIBAN(), body.getTransferAmount());
                    newBalance = account.getBalance();
                }

                ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.OK).body((JsonNode) objectMapper.createObjectNode().put("message", "Old balance was: "
                        + oldBalance.toString() + "\n New balance: " + newBalance.toString() + "\n deposit: +" + body.getTransferAmount().toString()));
                return responseEntity;

            } catch (Exception e) {
                ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                return responseEntity;
            }
        }
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body((JsonNode) objectMapper.createObjectNode().put("message", "Headers failed: Accept:" + accept + " & Content-Type:" + content));
        return responseEntity;
    }
}
