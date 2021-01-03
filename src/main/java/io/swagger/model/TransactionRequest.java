package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@NoArgsConstructor
@Validated
public class TransactionRequest {

    private String ibanSender;

    private String ibanReciever;

    private Double transferAmount;

    public TransactionRequest(String ibanSender, String ibanReciever, Double transferAmount) {
        this.ibanSender = ibanSender;
        this.ibanReciever = ibanReciever;
        this.transferAmount = transferAmount;
    }

    public String getIbanSender() {
        return ibanSender;
    }

    public String getIbanReciever() {
        return ibanReciever;
    }

    public Double getTransferAmount() {
        return transferAmount;
    }
}
