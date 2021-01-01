package io.swagger.model;

public class ATMRequest {

    private String IBAN;
    private Integer pincode;
    private Double transferAmount;

    public ATMRequest(String IBAN, Integer pincode, Double transferAmount) {
        this.IBAN = IBAN;
        this.pincode = pincode;
        this.transferAmount = transferAmount;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public Integer getPincode() {
        return pincode;
    }

    public void setPincode(Integer pincode) {
        this.pincode = pincode;
    }

    public Double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Double transferAmount) {
        this.transferAmount = transferAmount;
    }
}
