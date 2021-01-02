/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.19).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.annotations.*;
import io.swagger.model.Transaction;
import io.swagger.model.TransactionRequest;
import io.swagger.model.TransactionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-06-02T09:24:14.507Z[GMT]")
@Api(value = "transactions", description = "the transactions API")
public interface TransactionsApi {

    @ApiOperation(value = "Creating a transaction", nickname = "createTransaction", notes = "Creating a transaction", response = Transaction.class, tags={ "transactions", })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Succesful post.", response = Transaction.class),
            @ApiResponse(code = 401, message = "Authorization information is missing or invalid."),
            @ApiResponse(code = 404, message = "An account with the specified IBAN was not found."),
            @ApiResponse(code = 500, message = "Unexpected error.") })
    @RequestMapping(value = "/transactions",
            produces = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Transaction> create(@ApiParam(value = "Transaction created", required = true) @Valid @RequestBody TransactionRequest body);

    @ApiOperation(value = "Getting a transaction by seach", nickname = "searchTansaction", notes = "", response = TransactionResponse.class, responseContainer = "List", tags={ "transactions", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succesful request.", response = TransactionResponse.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Authorization information is missing or invalid."),
            @ApiResponse(code = 404, message = "An account with the specified IBAN was not found."),
            @ApiResponse(code = 500, message = "Unexpected error.") })
    @RequestMapping(value = "/transactions",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<List<TransactionResponse>> getAll();

    @ApiOperation(value = "Get a transaction by its id", nickname = "getTransactionById", notes = "Get a transaction by its id", response = Transaction.class, responseContainer = "List", tags={ "transactions", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succesful request.", response = Transaction.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad request, transactionId must be an integer and larger than 0."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "An account with the specified ID was not found."),
            @ApiResponse(code = 500, message = "Unexpected error.") })
    @RequestMapping(value = "transactions/{transactionId}",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<TransactionResponse> getById(@ApiParam(value = "id of a transaction", required=true) @PathVariable("transactionId") Long id);

    @ApiOperation(value = "Get a list of transactions", nickname = "getTransactionsForAccountByIBAN", notes = "Get a list of transactions for account by iban", response = Transaction.class, responseContainer = "List", tags={ "transactions", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succesful request.", response = Transaction.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad request."),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "An account with the specified Iban was not found."),
            @ApiResponse(code = 500, message = "Unexpected error.") })
    @RequestMapping(value = "transactions/{iban}/accounts",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<List<TransactionResponse>> getTransactionsForAccount(@ApiParam(value = "Iban of an account",required=true) @PathVariable("iban") String iban
    );

    @ApiOperation(value = "Getting a transaction by seach", nickname = "searchTansaction", notes = "", response = TransactionResponse.class, responseContainer = "List", tags={ "transactions", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succesful request.", response = TransactionResponse.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Authorization information is missing or invalid."),
            @ApiResponse(code = 404, message = "An account with the specified IBAN was not found."),
            @ApiResponse(code = 500, message = "Unexpected error.") })
    @RequestMapping(value = "/transactions/search",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<List<TransactionResponse>> searchTansaction(
            @ApiParam(value = "userPerformer") @Valid @RequestParam(value = "userPerformer", required = false) String userPerformer
            ,@ApiParam(value = "IBAN") @Valid @RequestParam(value = "IBAN", required = false) String IBAN
            ,@ApiParam(value = "transferAmount") @Valid @RequestParam(value = "transferAmount", required = false) Double transferAmount
            ,@ApiParam(value = "MaxNumberOfResults") @Valid @RequestParam(value = "MaxNumberOfResults", required = false) Integer maxNumberOfResults
    );
}