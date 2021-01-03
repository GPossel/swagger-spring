package io.swagger.model;

import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@NoArgsConstructor
@Validated
public class AccountRequest {

    private Long userId;
    private Account.RankEnum rank;
    private Account.StatusEnum status;

    public AccountRequest(Long userId, Account.RankEnum rank) {
        this.userId = userId;
        this.rank = rank;
    }

    public Long getUserId() {
        return userId;
    }
    public Account.RankEnum getRank() {
        return rank;
    }
    public Account.StatusEnum getStatus() {
        return status;
    }
}
