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
public class AccountResponse {
    @JsonProperty("iban")
    private String iban = null;

    @JsonProperty("userId")
    private Long userId = null;

    @JsonProperty("rank")
    private Account.RankEnum rank = null;

    @JsonProperty("balance")
    private Double balance = null;

    @JsonProperty("currency")
    private String currency = null;

    @JsonProperty("status")
    private Account.StatusEnum status = null;

    public AccountResponse(Account a) {
        this.iban = a.getIban();
        this.userId = a.getUserId();
        this.rank = a.getRank();
        this.balance = a.getBalance();
        this.currency = a.getCurrency();
        this.status = a.getStatus();
    }


}
