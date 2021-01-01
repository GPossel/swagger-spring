package io.swagger.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.model.*;
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

    @PreAuthorize("hasAuthority('Employee')")
    public ResponseEntity<AccountResponse> create(@ApiParam(value = "created accounts", required = true) @Valid @RequestBody AccountRequest body

    ) {
        System.out.println(1);
        System.out.println(body);
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-type");

        if (accept != null && content.contains("application/json")) {
            try {
                if (body != null) {
                    return new ResponseEntity<AccountResponse>(objectMapper.readValue(objectMapper.writeValueAsString(accountApiService.create(body)), AccountResponse.class), HttpStatus.CREATED);
                }
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<AccountResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (Exception e) {
                ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                return responseEntity;
            }
        }
        return new ResponseEntity<AccountResponse>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasAuthority('Employee')")
    public ResponseEntity<List<AccountResponse>> getAll(){
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-type");

        if (accept != null && content.contains("application/json")) {
                try {
                    Iterable<AccountResponse> accounts = accountApiService.getAll();
                    return new ResponseEntity<List<AccountResponse>>(objectMapper.readValue(objectMapper.writeValueAsString(accounts), List.class), HttpStatus.OK);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                    return responseEntity;
                }
            }
        return new ResponseEntity<List<AccountResponse>>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasAuthority('Employee')")
    public ResponseEntity<AccountResponse> getByIban(@ApiParam(value= "", required = true) @PathVariable("iban") String iban){
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-type");

        if (accept != null && content.contains("application/json")) {
                try {
                    AccountResponse account = accountApiService.getByIbanWithAuth(iban);
                    return new ResponseEntity<AccountResponse>(objectMapper.readValue(objectMapper.writeValueAsString(account), AccountResponse.class), HttpStatus.OK);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                    return responseEntity;
                }
            }
        return new ResponseEntity<AccountResponse>(HttpStatus.BAD_REQUEST);
    }

    /*Delete User*/
    @PreAuthorize("hasAuthority('Employee')")
    public ResponseEntity<Void> delete(@ApiParam(value = "The iban that needs to be deleted", required = true) @PathVariable("iban") String iban) {
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-type");

        if (accept != null && content.contains("application/json")) {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User loggedInUser = (User)authentication.getPrincipal();
                accountApiService.delete(iban);
                ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.OK).body((JsonNode) objectMapper.createObjectNode().put("message", "Deleted Successfully!"));
                return responseEntity;
            } catch (Exception e) {
                ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                return responseEntity;
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('Employee') or hasAuthority('Customer')")
    public ResponseEntity<List<AccountResponse>> getAccountsForUser(@ApiParam(value= "", required = true) @PathVariable("userid") Long userId){
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-Type");
        if (accept != null && content.contains("application/json")) {
                try {
                    Iterable<AccountResponse> accounts = accountApiService.responseGetAccountsForUser(userId);
                    return new ResponseEntity<List<AccountResponse>>(objectMapper.readValue(objectMapper.writeValueAsString(accounts), List.class), HttpStatus.OK);
                } catch (IOException e) {
                    log.error("Couldn't serialize response for content type application/json", e);
                    ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                    return responseEntity;
                }
            }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasAuthority('Employee') or hasAuthority('Customer')")
    public ResponseEntity<ATMResponse> withdraw(@ApiParam(value = "created withdraw", required = true) @Valid @RequestBody ATMRequest body)
    {
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-Type");

        if (accept != null && content.contains("application/json")) {
            try {
                ATMResponse atmResponse = accountApiService.withdraw(body);
                return new ResponseEntity<ATMResponse>(objectMapper.readValue(objectMapper.writeValueAsString(atmResponse), ATMResponse.class), HttpStatus.OK);
            } catch (IOException e) {
                ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                return responseEntity;
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasAuthority('Employee') or hasAuthority('Customer')")
    public ResponseEntity<ATMResponse> deposit(@ApiParam(value = "created deposit", required = true) @Valid @RequestBody ATMRequest body)
    {
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-Type");

        if (accept != null && content.contains("application/json")) {
            try {
                ATMResponse atmResponse = accountApiService.deposit(body);
                return new ResponseEntity<ATMResponse>(objectMapper.readValue(objectMapper.writeValueAsString(atmResponse), ATMResponse.class), HttpStatus.OK);
            } catch (IOException e) {
                ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                return responseEntity;
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}