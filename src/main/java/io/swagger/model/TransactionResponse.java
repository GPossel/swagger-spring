package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@Validated
@NoArgsConstructor
public class TransactionResponse {

    @JsonProperty("ibanSender")
    private String ibanSender = null;

    @JsonProperty("ibanReciever")
    private String ibanReciever = null;

    @JsonProperty("transactionDate")
    private Timestamp transactionDate = null;

    @JsonProperty("transferAmount")
    private Double transferAmount = null;

    @JsonProperty("userSender")
    private String userSender = null;

    @JsonProperty("nameSender")
    private String nameSender = null;

    @JsonProperty("nameReciever")
    private String nameReciever = null;

    public TransactionResponse(Transaction t, String userName, String nameSender, String nameReciever) {
            this.ibanSender = t.getIbanSender();
            this.ibanReciever = t.getIbanReciever();
            this.transactionDate = t.getTransactionDate();
            this.transferAmount = t.getTransferAmount();
            this.userSender = userName;
            this.nameSender = nameSender;
            this.nameReciever = nameReciever;
    }

    public TransactionResponse(Transaction t) {
        this.ibanSender = t.getIbanSender();
        this.ibanReciever = t.getIbanReciever();
        this.transactionDate = t.getTransactionDate();
        this.transferAmount = t.getTransferAmount();
    }


    public String getUserSender() {
        return userSender;
    }

    public String getNameSender() {
        return nameSender;
    }

    public String getNameReciever() {
        return nameReciever;
    }
}
