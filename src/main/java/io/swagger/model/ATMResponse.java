package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@NoArgsConstructor
@Validated
public class ATMResponse {
    @JsonProperty("iban")
    private String iban = null;

    @JsonProperty("balance")
    private Double balance = null;

    @JsonProperty("oldBalance")
    private Double oldBalance = null;

    public ATMResponse(Account a, Double oldBalance) {
        this.iban = a.getIban();
        this.balance = a.getBalance();
        this.oldBalance = oldBalance;
    }
}
