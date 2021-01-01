package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionRequest {

    private String ibanSender;

    private String ibanReceiver;

    private Double transferAmount;

    public TransactionRequest(String ibanSender, String ibanReceiver, Double transferAmount) {
        this.ibanSender = ibanSender;
        this.ibanReceiver = ibanReceiver;
        this.transferAmount = transferAmount;
    }

    public String getIbanSender() {
        return ibanSender;
    }

    public String getIbanReceiver() {
        return ibanReceiver;
    }

    public Double getTransferAmount() {
        return transferAmount;
    }
}
