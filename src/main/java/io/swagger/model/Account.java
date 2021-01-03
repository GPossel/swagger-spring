package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

/**
 * Account
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "Accounts", uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "IBAN"})})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-18T09:28:40.437Z[GMT]")
@Data
public class Account {
  @Id
  @JsonProperty("iban")
  @Column(name = "iban")
  private String iban = null;

  @JsonProperty("userId")
  @Column(name = "USERID")
  private Long userId = null;

  @JsonProperty("rank")
  private RankEnum rank = null;

  @JsonProperty("balance")
  private Double balance = null;

  @JsonProperty("currency")
  private String currency = null;

  @JsonProperty("dailyLimit")
  private Double dailyLimit = null;

  @JsonProperty("transferLimit")
  private Double transferLimit = null;

  @JsonProperty("status")
  private StatusEnum status = null;

  public Account(String IBAN, Long userId, RankEnum rank, StatusEnum status, Double balance, String currency) {
    this.userId = userId;
    this.iban = IBAN;
    this.rank = rank;
    this.status = status;
    this.balance = balance;
    this.currency = currency;
    this.dailyLimit = 10000d;
    this.transferLimit = 500d;
  }

  public Account(AccountRequest accountRequest) {
    setIban();
    this.userId = accountRequest.getUserId();
    this.rank = accountRequest.getRank();
    this.status = StatusEnum.ACTIVE;
    setBalance(0d);
    this.currency = "EUR";
    this.dailyLimit = 10000d;
    this.transferLimit = 500d;
  }

  public Account(String nlfout) {
    this.iban = nlfout;
  }

  public Double getDailyLimit() {
    return dailyLimit;
  }

  public void setDailyLimit(Double dailyLimit) {
    this.dailyLimit = dailyLimit;
  }

  public Double getTransferLimit() {
    return transferLimit;
  }

  public void setTransferLimit(Double transferLimit) {
    this.transferLimit = transferLimit;
  }

  /**
   * Gets or Sets rank
   */
  public enum RankEnum {
    CURRENT("Current"),
    SAVING("Saving"),
    BANK("Bank");

    private String value;

    RankEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static RankEnum fromValue(String text) {
      for (RankEnum b : RankEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("Active"),
    BLOCKED("Blocked"),
    DELETED("Deleted");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }



  public Account userId(Long userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   *
   * @return userId
   **/
  @ApiModelProperty(example = "2", required = true, value = "")
  @NotNull

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Account IBAN(String IBAN) {
    this.iban = IBAN;
    return this;
  }

  /**
   * Get IBAN
   *
   * @return IBAN
   **/
  @ApiModelProperty(example = "NLxxINHO0xxxxxxxxx", required = true, value = "")
  @NotNull

  public String getIban() {
    return iban;
  }

  public void setIban() {
    Random random = new Random();
    String prefix1 = "NL";
    String firstDigits = String.format("%02d", random.nextInt(100));
    String prefix2 = "INHO"; // contains one of the 10 numbers: 0
    String lastDigits = String.format("%09d", random.nextInt(1000000000));

    this.iban = prefix1 + firstDigits + prefix2 + lastDigits;
    }

  public Account rank(RankEnum rank) {
    this.rank = rank;
    return this;
  }

  /**
   * Get rank
   *
   * @return rank
   **/
  @ApiModelProperty(example = "Current", value = "")

  public RankEnum getRank() {
    return rank;
  }

  public void setRank(RankEnum rank) {
    this.rank = rank;
  }

  public Account balance(Double balance) {
    this.balance = balance;
    return this;
  }

  /**
   * Get balance
   * minimum: 0
   *
   * @return balance
   **/
  @ApiModelProperty(example = "100", required = true, value = "")
  @NotNull

  @DecimalMin("0")
  public Double getBalance() {
    return balance;
  }

  public void setBalance(Double balance) {
    if (balance == null){
      balance = 0d;
    }
    if (balance < 0){
      throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "balance is invalid");
    }

    this.balance = balance;
  }

  public Account currency(String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Get currency
   *
   * @return currency
   **/
  @ApiModelProperty(example = "EUR", value = "")

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public Account status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   *
   * @return status
   **/
  @ApiModelProperty(example = "Active", value = "")

  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Account account = (Account) o;
    return Objects.equals(this.userId, account.userId) &&
            Objects.equals(this.iban, account.iban) &&
            Objects.equals(this.rank, account.rank) &&
            Objects.equals(this.balance, account.balance) &&
            Objects.equals(this.currency, account.currency) &&
            Objects.equals(this.status, account.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, iban, rank, balance, currency, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Account {\n");

    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    IBAN: ").append(toIndentedString(iban)).append("\n");
    sb.append("    rank: ").append(toIndentedString(rank)).append("\n");
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    dailyLimit: ").append(toIndentedString(dailyLimit)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  public int cumulativeTransactions = 0;
  public LocalDateTime start = null;
  public LocalDateTime end = null;

  public boolean isPassedCumulativeTransactions() {

    if (start == null) {
      start = LocalDateTime.now();
      end = LocalDateTime.now().plusHours(24);
    }

    if (cumulativeTransactions < 3) {
      cumulativeTransactions++;
    } else {
      start = LocalDateTime.now();
      if (start.isAfter(end)) {
        cumulativeTransactions = 0;
        start = null;
        end = null;
        return false;
      }
    }
    return false;
  }

}
