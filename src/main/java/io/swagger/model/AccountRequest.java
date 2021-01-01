package io.swagger.model;

public class AccountRequest {

    private Long userId;
    private Account.RankEnum rank;

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
}
