package io.swagger.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.model.Transaction;
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

    @PreAuthorize("hasAuthority('Employee')")
    public ResponseEntity<List<Transaction>> getTransactions() {
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-Type");
        if (accept != null && content.contains("application/json")) {
            try {
                List<Transaction> myList = (List<Transaction>) transactionApiService.getTransactions();
                return new ResponseEntity<List<Transaction>>(objectMapper.readValue(objectMapper.writeValueAsString(myList), List.class), HttpStatus.OK);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Transaction>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Transaction>>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PreAuthorize("hasAuthority('Employee') or hasAuthority('Customer')")
    public ResponseEntity<Transaction> getTransaction(@Min(0L) @ApiParam(value = "", required = true, allowableValues = "") @PathVariable("transactionId") Long transactionId
    ) {
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-Type");
        Transaction transaction = transactionApiService.getTransaction(transactionId);
        if (accept != null && content.contains("application/json")) {
            return status(HttpStatus.OK).body(transaction);
        }
        return status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PreAuthorize("hasAuthority('Employee') or hasAuthority('Customer')")
    public ResponseEntity<List<Transaction>> searchTansaction
            // transaction moet niet terug sturen wie de performer is, wel de iban
            // user performer naam
            (@ApiParam(value = "") @Valid @RequestParam(value = "userPerformer", required = false) Long userPerformer,
             // transactionId moet weg, ook niet terug geven
             @ApiParam(value = "") @Valid @RequestParam(value = "transactionId", required = false) Long transactionId,
             @ApiParam(value = "") @Valid @RequestParam(value = "IBAN", required = false) String IBAN,
             @ApiParam(value = "") @Valid @RequestParam(value = "transferAmount", required = false) Double transferAmount,
             @ApiParam(value = "") @Valid @RequestParam(value = "MaxNumberOfResults", required = false) Integer maxNumberOfResults) {
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-Type");
        if (accept != null && content.contains("application/json")) {
            try {
                    List<Transaction> myList = transactionApiService.FindAllMatches(userPerformer, transactionId, IBAN, transferAmount, maxNumberOfResults);
                    return new ResponseEntity<List<Transaction>>(objectMapper.readValue(objectMapper.writeValueAsString(myList), List.class), HttpStatus.OK);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Transaction>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<List<Transaction>>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PreAuthorize("hasAuthority('Employee') or hasAuthority('Customer')")
    public ResponseEntity<Transaction> transferFunds(@ApiParam(value = "Transaction object", required = true) @Valid @RequestBody Transaction body) {
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-Type");
        if (accept != null && content.contains("application/json")) {
            try {
                Transaction transaction = new Transaction(body.getIbanSender(), body.getIbanReceiver(), body.getTransferAmount());
                // adding the userperformer and transaction time data
                transaction = transactionApiService.FillInTransactionSpecifics(transaction);

                transactionApiService.checkValidTransaction(transaction);
                Boolean transSucces = transactionApiService.makeTransaction(transaction);
                if (transSucces == true) {
                    return new ResponseEntity<Transaction>(objectMapper.readValue(objectMapper.writeValueAsString(transaction), Transaction.class), HttpStatus.CREATED);
                } else {
                    ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((JsonNode) objectMapper.createObjectNode().put("message", "The transaction was not succesful"));
                    return responseEntity;
                }
            } catch (Exception e) {
                ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                return responseEntity;
            }
        }
        return null;
    }
}

