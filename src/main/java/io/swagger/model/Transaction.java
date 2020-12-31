package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Transaction
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"transactionId"})})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Validated
@Data
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-18T09:28:40.437Z[GMT]")
public class Transaction {
  @Id
  @SequenceGenerator(name="transaction_seq", initialValue = 1000001)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="transaction_seq")
  @JsonProperty("transactionId")
  @Column(unique=true, nullable=false)
  private Long transactionId = null;

  @JsonProperty("ibanSender")
  private String ibanSender = null;

  @JsonProperty("ibanReceiver")
  private String ibanReceiver = null;

  @JsonProperty("userPerformer")
  private Long userPerformer = null;

  @JsonProperty("transactionDate")
  private Timestamp transactionDate = null;

  @JsonProperty("transferAmount")
  private Double transferAmount = null;

  @JsonProperty("nameSender")
  private String nameSender = null;

  public Transaction()
  {}

  public Transaction(String ibanSender, String ibanReceiver, Long userPerformer, String transactionDate, Double transferAmount) {
    this.ibanSender = ibanSender;
    this.ibanReceiver = ibanReceiver;
    this.userPerformer = userPerformer;
    this.transferAmount = transferAmount;
    setTransactionDate(transactionDate);
  }

  //RESPONSE
  public Transaction(String nameSender, String ibanSender, String ibanReceiver, Double transferAmount, Timestamp transactionDate) {
    this.ibanSender = ibanSender;
    this.ibanReceiver = ibanReceiver;
    this.nameSender = nameSender;
    this.transactionDate = transactionDate;
    this.transferAmount = transferAmount;
  }


  public Transaction(TransactionRequest t, Long userPerformer) {
    setIbanSender(t.getIbanSender());
    setIbanReceiver(t.getIbanReceiver());
    this.transactionDate = new Timestamp(new Date().getTime());
    setTransferAmount(t.getTransferAmount());
    this.userPerformer = userPerformer;
  }


  /**
   * Get ibanSender
   *
   * @return ibanSender
   **/
  @ApiModelProperty(value = "")

  public String getIbanSender() {
    if (!ibanSender.matches("NL\\d\\dINHO\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d")) {
      throw new IllegalArgumentException("IBAN MUST BE TYPE OF NLXXINHOXXXXXXXXXX");
    }
    return ibanSender;
  }

  public void setIbanSender(String ibanSender) {
    if (!ibanSender.matches("NL\\d\\dINHO\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d")) {
      throw new IllegalArgumentException("IBAN MUST BE TYPE OF NLXXINHOXXXXXXXXXX");
    }
    this.ibanReceiver = ibanReceiver;
  }

  /**
   * Get ibanReceiver
   *
   * @return ibanReceiver
   * @return ibanReceiver
   **/
  @ApiModelProperty(value = "")

  public String getIbanReceiver() {
  if (!ibanReceiver.matches("NL\\d\\dINHO\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d")) {
    throw new IllegalArgumentException("IBAN MUST BE TYPE OF NLXXINHOXXXXXXXXXX");
  }
    return ibanReceiver;
  }

  public void setIbanReceiver(String ibanReceiver) {
    if(!ibanReceiver.matches("NL\\d\\dINHO\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d"))
    {
      throw new IllegalArgumentException("IBAN MUST BE TYPE OF NLXXINHOXXXXXXXXXX");
    }
    this.ibanReceiver = ibanReceiver;
  }
  /**
   * Get transactionId
   *
   * @return transactionId
   **/
  @ApiModelProperty(value = "")
  public Long getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(Long transactionId) {
    this.transactionId = transactionId;
  }

  /**
   * Get nameSender
   * @return nameSender
   **/
  @ApiModelProperty(value = "")

  public Long getUserPerformer() {
    return userPerformer;
  }

  public void setUserPerformer(Long userPerformer) {
    this.userPerformer = userPerformer;
  }

  /**
   * Get transactionDate
   * @return transactionDate
   **/
  @ApiModelProperty(value = "")

  public Timestamp getTransactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(String transactionDate) {
    try {
      DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
      this.transactionDate = new Timestamp(dateFormat.parse(transactionDate).getTime());
    } catch (Exception e){
      throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"The registrationDate is invalid");
    }
  }

  public Transaction transferAmount(Double transferAmount) {
    this.transferAmount = transferAmount;
    return this;
  }

  /**
   * Get transferAmount
   * @return transferAmount
   **/
  @ApiModelProperty(value = "")

  public Double getTransferAmount() {
    return transferAmount;
  }

  public void setTransferAmount(Double transferAmount) {
    if (transferAmount <= 0) {
      throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Transaction must be above 0");
    }
    this.transferAmount = transferAmount;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Transaction transaction = (Transaction) o;
    return Objects.equals(this.ibanSender, transaction.ibanSender) &&
            Objects.equals(this.ibanReceiver, transaction.ibanReceiver) &&
            Objects.equals(this.transactionId, transaction.transactionId) &&
            Objects.equals(this.userPerformer, transaction.userPerformer) &&
            Objects.equals(this.transactionDate, transaction.transactionDate) &&
            Objects.equals(this.transferAmount, transaction.transferAmount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ibanSender, ibanReceiver, transactionId, userPerformer, transactionDate, transferAmount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Transaction {\n");

    sb.append("    ibanSender: ").append(toIndentedString(ibanSender)).append("\n");
    sb.append("    ibanReceiver: ").append(toIndentedString(ibanReceiver)).append("\n");
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    userPerformer: ").append(toIndentedString(userPerformer)).append("\n");
    sb.append("    transactionDate: ").append(toIndentedString(transactionDate)).append("\n");
    sb.append("    transferAmount: ").append(toIndentedString(transferAmount)).append("\n");
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

  public String getNameSender() {
    return nameSender;
  }

  public void setNameSender(String nameSender) {
    this.nameSender = nameSender;
  }

}
