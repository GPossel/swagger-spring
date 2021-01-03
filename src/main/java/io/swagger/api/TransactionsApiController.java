package io.swagger.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.model.Transaction;
import io.swagger.model.TransactionRequest;
import io.swagger.model.TransactionResponse;
import io.swagger.service.AccountApiService;
import io.swagger.service.TransactionApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.ResponseEntity.status;


@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-06-02T09:24:14.507Z[GMT]")
@Controller
public class TransactionsApiController implements TransactionsApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private TransactionApiService transactionApiService;

    @Autowired
    private AccountApiService accountApiService;

    @Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @PreAuthorize("hasAuthority('Employee') or hasAuthority('Customer')")
    public ResponseEntity<Transaction> create(@ApiParam(value = "Transaction object", required = true) @Valid @RequestBody TransactionRequest body) {
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-Type");

        if (accept != null && content.contains("application/json")) {
            try {
                Transaction transaction = transactionApiService.create(body);
                return new ResponseEntity<Transaction>(objectMapper.readValue(objectMapper.writeValueAsString(transaction), Transaction.class), HttpStatus.CREATED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                return responseEntity;
            }
        }
        return new ResponseEntity<Transaction>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasAuthority('Employee') or hasAnyAuthority('Customer')")
    public ResponseEntity<List<TransactionResponse>> getAll() {
        String accept = request.getHeader("Accept");
        if (accept != null) {
            try {
                List<TransactionResponse> transactions = transactionApiService.getAllTransactionsResponses();
                return new ResponseEntity<List<TransactionResponse> >(objectMapper.readValue(objectMapper.writeValueAsString(transactions), List.class), HttpStatus.OK);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<TransactionResponse> >(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<TransactionResponse> >(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasAuthority('Employee') or hasAnyAuthority('Customer')")
    public ResponseEntity<List<TransactionResponse>> getTransactionsForAccount(@ApiParam(value = "Iban of an account", required = true) @PathVariable("iban") String iban) {
        String accept = request.getHeader("Accept");
        if (accept != null) {
            try {
                List<TransactionResponse> myList = transactionApiService.getTransactionsForAccountByIBAN(iban);
                return new ResponseEntity<List<TransactionResponse>>(objectMapper.readValue(objectMapper.writeValueAsString(myList), List.class), HttpStatus.OK);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<TransactionResponse>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<TransactionResponse>>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PreAuthorize("hasAuthority('Employee') or hasAuthority('Customer')")
    public ResponseEntity<TransactionResponse> getById(@ApiParam(value = "id of transaction", required = true) @PathVariable("transactionId") Long id
    ) {
        String accept = request.getHeader("Accept");
        if (accept != null) {
            try {
                TransactionResponse transaction = transactionApiService.getById(id);
                return new ResponseEntity<TransactionResponse>(objectMapper.readValue(objectMapper.writeValueAsString(transaction), TransactionResponse.class), HttpStatus.OK);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<TransactionResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return status(HttpStatus.BAD_REQUEST).build();
    }

    @PreAuthorize("hasAuthority('Employee') or hasAuthority('Customer')")
    public ResponseEntity<List<TransactionResponse>> searchTansaction
            (@ApiParam(value = "") @Valid @RequestParam(value = "userPerformer", required = false) String userPerformer,
             @ApiParam(value = "") @Valid @RequestParam(value = "IBAN", required = false) String IBAN,
             @ApiParam(value = "") @Valid @RequestParam(value = "transferAmount", required = false) Double transferAmount,
             @ApiParam(value = "") @Valid @RequestParam(value = "MaxNumberOfResults", required = false) Integer maxNumberOfResults) {
        String accept = request.getHeader("Accept");
        if (accept != null) {
            try {
                List<TransactionResponse> transactions = transactionApiService.FindAllMatches(userPerformer, IBAN, transferAmount, maxNumberOfResults);
                return new ResponseEntity<List<TransactionResponse>>(objectMapper.readValue(objectMapper.writeValueAsString(transactions), List.class), HttpStatus.OK);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<TransactionResponse>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<List<TransactionResponse>>(HttpStatus.BAD_REQUEST);
    }
}

