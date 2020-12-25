/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.19).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.annotations.*;
import io.swagger.model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-06-02T09:24:14.507Z[GMT]")
@Api(value = "transactions", description = "the transactions API")
public interface TransactionsApi {

    @ApiOperation(value = "Getting a list of transactions", nickname = "getTransactions", notes = "", response = Transaction.class, responseContainer = "List", tags={ "transactions", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succesful request.", response = Transaction.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad request. Account ID must be an integer and larger than 0."),
            @ApiResponse(code = 401, message = "Authorization information is missing or invalid."),
            @ApiResponse(code = 404, message = "An account with the specified ID was not found."),
            @ApiResponse(code = 500, message = "Unexpected error.") })
    @RequestMapping(value = "transactions/{accountId}",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<Transaction> getTransaction(@Min(0L)@ApiParam(value = "",required=true, allowableValues="") @PathVariable("transactionId") Long transactionId
    );

    @ApiOperation(value = "Getting a transaction by seach", nickname = "searchTansaction", notes = "", response = Transaction.class, responseContainer = "List", authorizations = {
            @Authorization(value = "ApiKeyAuth")    }, tags={ "transactions", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succesful request.", response = Transaction.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Authorization information is missing or invalid."),
            @ApiResponse(code = 404, message = "An account with the specified IBAN was not found."),
            @ApiResponse(code = 500, message = "Unexpected error.") })
    @RequestMapping(value = "/transactions",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<List<Transaction>> searchTansaction(@ApiParam(value = "nameSender") @Valid @RequestParam(value = "userPerformer", required = false) Long userPerformer
            ,@ApiParam(value = "transactionId") @Valid @RequestParam(value = "transactionId", required = false) Long transactionId
            ,@ApiParam(value = "IBAN") @Valid @RequestParam(value = "IBAN", required = false) String IBAN
            ,@ApiParam(value = "transferAmount") @Valid @RequestParam(value = "transferAmount", required = false) Double transferAmount
            ,@ApiParam(value = "MaxNumberOfResults") @Valid @RequestParam(value = "MaxNumberOfResults", required = false) Integer maxNumberOfResults
    );


    @ApiOperation(value = "Getting a transaction by seach", nickname = "searchTansaction", notes = "", response = Transaction.class, responseContainer = "List", authorizations = {
            @Authorization(value = "ApiKeyAuth")    }, tags={ "transactions", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succesful request.", response = Transaction.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Authorization information is missing or invalid."),
            @ApiResponse(code = 404, message = "An account with the specified IBAN was not found."),
            @ApiResponse(code = 500, message = "Unexpected error.") })
    @RequestMapping(value = "/transactions/all",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<List<Transaction>> getTransactions();

    @ApiOperation(value = "Making a transaction", nickname = "transferFunds", notes = "", response = Transaction.class, responseContainer = "List", authorizations = {
            @Authorization(value = "ApiKeyAuth")    }, tags={ "transactions", })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Succesful post.", response = Transaction.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Authorization information is missing or invalid."),
            @ApiResponse(code = 404, message = "An account with the specified IBAN was not found."),
            @ApiResponse(code = 500, message = "Unexpected error.") })
    @RequestMapping(value = "/transactions",
            produces = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Transaction> transferFunds(@ApiParam(value = "Transaction created", required = true) @Valid @RequestBody Transaction transaction);
}