/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.19).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.*;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-06-02T09:24:14.507Z[GMT]")
@Api(value = "accounts", description = "the accounts API")
public interface AccountsApi {
    @ApiOperation(value = "Add a new account", nickname = "createAccount", notes = "", response = AccountResponse.class, tags={ "accounts", })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "A account was created succesfully.", response = AccountResponse.class),
            @ApiResponse(code = 400, message = "invalid operation"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @RequestMapping(value = "/accounts",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<AccountResponse> create(@ApiParam(value = "created accounts" , required=true )  @Valid @RequestBody AccountRequest body);

    @ApiOperation(value = "Get all Accounts", nickname = "getAll", notes = "Get a list of accounts", response = AccountResponse.class, responseContainer = "List", tags={ "accounts", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "succesful operation", response = AccountResponse.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "invalid operation"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @RequestMapping(value = "/accounts",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<AccountResponse>> getAll(@ApiParam(value = "rank of an account", required = false) @Valid @RequestParam (value = "rank", required = false) Account.RankEnum rank,
                                                 @ApiParam(value = "status of an account", required = false) @Valid @RequestParam (value = "status", required = false)Account.StatusEnum status);

    @ApiOperation(value = "Get Accounts by iban", nickname = "getAccountByIban", notes = "Get an account by its id", response = AccountResponse.class, tags={ "accounts", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "account by iban", response = AccountResponse.class),
            @ApiResponse(code = 400, message = "invalid operation"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @RequestMapping(value = "/accounts/{iban}",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<AccountResponse> getByIban(@ApiParam(value = "",required=true) @PathVariable("iban") String iban);

    @ApiOperation(value = "Delete account", nickname = "deleteAccount", notes = "Deletes a account", tags={ "accounts", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully deleted!"),
            @ApiResponse(code = 400, message = "invalid operation"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @RequestMapping(value = "/accounts/{iban}",
            method = RequestMethod.DELETE)
    ResponseEntity<Void> delete(@ApiParam(value = "The IBAN that needs to be deleted",required=true) @PathVariable("iban") String iban
    );


    @ApiOperation(value = "Get Accounts for user by its id", nickname = "getAccountsForUser", notes = "Get a list of accounts for user", response = AccountResponse.class, responseContainer=  "List", tags = { "accounts", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Accounts for user by its id", response = AccountResponse.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "invalid operation"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @RequestMapping(value = "/accounts/{userid}/users",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<List<AccountResponse>> getAccountsForUser(@ApiParam(value = "",required=true) @PathVariable("userid") Long userId);

    @ApiOperation(value = "Making a withdraw", nickname = "withdraw", notes = "Making a withdraw", response = ATMResponse.class, tags={ "accounts" })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Succesful withdraw.", response = ATMResponse.class),
            @ApiResponse(code = 403, message = "Authorization information is missing or invalid."),
            @ApiResponse(code = 404, message = "An account with the specified IBAN was not found."),
            @ApiResponse(code = 500, message = "Unexpected error.") })
    @RequestMapping(value = "/accounts/withdraw",
            produces = { "application/json" },
            method = RequestMethod.PUT)
    ResponseEntity<ATMResponse> withdraw(@ApiParam(value = "created withdraw" , required=true)  @Valid @RequestBody ATMRequest body);


    @ApiOperation(value = "Making a deposit", nickname = "deposit", notes = "", response = ResponseEntity.class, responseContainer = "List", tags={ "accounts" })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Succesful withdraw.", response = ResponseEntity.class, responseContainer = "List"),
            @ApiResponse(code = 403, message = "Authorization information is missing or invalid."),
            @ApiResponse(code = 404, message = "An account with the specified IBAN was not found."),
            @ApiResponse(code = 500, message = "Unexpected error.") })
    @RequestMapping(value = "/accounts/deposit",
            produces = { "application/json" },
            method = RequestMethod.PUT)
    ResponseEntity deposit(@ApiParam(value = "created deposit" , required=true)  @Valid @RequestBody ATMRequest body);
}
