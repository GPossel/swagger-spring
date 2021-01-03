package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-18T09:28:40.437Z[GMT]")
public class User implements UserDetails{
  @Id
  @SequenceGenerator(name="transaction_seq", initialValue = 1000001)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="transaction_seq")
  @JsonProperty("id")
  @Column(unique = true, nullable = false)
  private Long id = null;

  @JsonProperty("firstname")
  private String firstname = null;

  @JsonProperty("lastname")
  private String lastname = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("password")
  private String password = null;

  @JsonProperty("phone")
  private String phone = null;

  @JsonProperty("birthdate")
  private Date birthdate = null;

  @JsonProperty("registrationdate")
  private Timestamp registrationdate = null;

  public User(UserRequest u) {
    setFirstname(u.getFirstname());
    setLastname(u.getLastname());
    setEmail(u.getEmail());
    setPassword(u.getPassword(), u.getrPassword());
    setPhone(u.getPhone());
    setBirthdate(u.getBirthdate());
    this.registrationdate = new Timestamp(new Date().getTime());
    setRank(u.getRank());
    this.status = StatusEnum.ACTIVE;
  }

    public User(long id, String firstname, String lastname, String email, String password, String phone, String birthdate, String registrationdate, RankEnum rank, StatusEnum status) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        setPasswordEncrypt(password);
        this.phone = phone;
        setBirthdate(birthdate);
        setRegistrationdate(registrationdate);
        this.rank = rank;
        this.status = status;
  }

    /**
   * User Rank
   */
  public enum RankEnum {
    CUSTOMER("Customer"),

    EMPLOYEE("Employee"),

    ADMIN("Admin");

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
  @JsonProperty("rank")
  private RankEnum rank = null;

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
  @JsonProperty("status")
  private StatusEnum status = null;

  public User id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   **/
  @ApiModelProperty(value = "")

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User firstname(String firstname) {
    this.firstname = firstname;
    return this;
  }

  /**
   * Get firstname
   * @return firstname
   **/
  @ApiModelProperty(value = "")

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    if (firstname == null || firstname.isEmpty()) {
      return;
    } else if (!firstname.matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$")){
      throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The first or lastname is not valid!");
    }

    this.firstname = firstname;
  }

  public User lastname(String lastname) {
    this.lastname = lastname;
    return this;
  }

  /**
   * Get lastname
   * @return lastname
   **/
  @ApiModelProperty(value = "")

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    if (lastname == null || lastname.isEmpty()) {
      return;
    } else if (!lastname.matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$")){
      throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The first or lastname is not valid!");
    }
    this.lastname = lastname;
  }

  public User email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   **/
  @ApiModelProperty(value = "")

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    if (email == null || email.isEmpty()) {
      return;
    } else if (!email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")){
      throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The Email is not valid!");
    }
    this.email = email;
  }

  public User password(String password) {
    this.password = password;
    return this;
  }

   String ROLE_PREFIX = "ROLE_";

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
//    String role = this.rank.toString(); // "ROLE_EMPLOYEE";//
//    String status = this.status.toString(); // "ROLE_Active / ROLE_ACTIVE / Active //"
    authorities.add(new SimpleGrantedAuthority(this.rank.toString()));
    authorities.add(new SimpleGrantedAuthority(this.status.toString()));
    return authorities;
  }

  /**
   * Get password
   * @return password
   **/
  @ApiModelProperty(value = "")

  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return this.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public void setPassword(String password, String rPassword) {
    if (!Objects.equals(password, rPassword)){
      throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Password and repeat password are not identical");
    }
    if (!password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$")){
      throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Password is not valid!");
    }
    setPasswordEncrypt(password);
  }

  public void setPasswordEncrypt(String password){
    String passwordEncrypted = new BCryptPasswordEncoder().encode(password);
    this.password = passwordEncrypted;
  }

  public User phone(String phone) {
    this.phone = phone;
    return this;
  }

  /**
   * Get phone
   * @return phone
   **/
  @ApiModelProperty(value = "")

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    if (!phone.matches("^((\\+|00(\\s|\\s?\\-\\s?)?)31(\\s|\\s?\\-\\s?)?(\\(0\\)[\\-\\s]?)?|0)[1-9]((\\s|\\s?\\-\\s?)?[0-9])((\\s|\\s?-\\s?)?[0-9])((\\s|\\s?-\\s?)?[0-9])\\s?[0-9]\\s?[0-9]\\s?[0-9]\\s?[0-9]\\s?[0-9]$")){
      throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The Phone number is invalid");
    }
    this.phone = phone;
  }

  /**
   * Get birthdate
   * @return birthdate
   **/
  @ApiModelProperty(value = "")

  public Date getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(String birthdate) {
    if (!birthdate.toString().matches("^(19|20)\\d{2}(-)(((0)[1-9])|([1-9])|((1)[0-2]))(-)([1-9]|(0)[1-9]|[1-2][0-9]|(3)[0-1])$")){
      throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"The birthdate is invalid");
    }

    try {
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      this.birthdate = dateFormat.parse(birthdate);
    } catch (Exception e){
      throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"The birthdate is invalid");
    }
  }

  /**
   * Get registrationdate
   * @return registrationdate
   **/
  @ApiModelProperty(value = "")

  public Date getRegistrationdate() {
    return registrationdate;
  }

  public void setRegistrationdate(String registrationdate) {
    try {
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
      this.registrationdate = new Timestamp(dateFormat.parse(registrationdate).getTime());
    } catch (Exception e){
      throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"The registrationDate is invalid");
    }  }

  public User rank(RankEnum rank) {
    this.rank = rank;
    return this;
  }

  /**
   * User Rank
   * @return rank
   **/
  @ApiModelProperty(value = "User Rank")

  public RankEnum getRank() {
    return rank;
  }

  public void setRank(RankEnum rank) {
    this.rank = rank;
  }

  public User status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   **/
  @ApiModelProperty(value = "")

  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public User checkNull(User user, UserRequest body) throws IllegalAccessException, NoSuchFieldException {
    for (Field f: body.getClass().getDeclaredFields()){
      if (f.get(body) != null){
        for (Field f2 : user.getClass().getDeclaredFields()){
          if (f2.getName().equals(f.getName())){
            if (f.getName().equals("password")){
              setPassword(body.getPassword(), body.getrPassword());
              body.getClass().getField("rPassword").set(body, null);
            }
            else {
              f2.set(user, f.get(body).toString());
            }
          }
        }
      }
    }
    return user;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(this.id, user.id) &&
            Objects.equals(this.firstname, user.firstname) &&
            Objects.equals(this.lastname, user.lastname) &&
            Objects.equals(this.email, user.email) &&
            Objects.equals(this.password, user.password) &&
            Objects.equals(this.phone, user.phone) &&
            Objects.equals(this.birthdate, user.birthdate) &&
            Objects.equals(this.registrationdate, user.registrationdate) &&
            Objects.equals(this.rank, user.rank) &&
            Objects.equals(this.status, user.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstname, lastname, email, password, phone, birthdate, registrationdate, rank, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class User {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    firstname: ").append(toIndentedString(firstname)).append("\n");
    sb.append("    lastname: ").append(toIndentedString(lastname)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    birthdate: ").append(toIndentedString(birthdate)).append("\n");
    sb.append("    registrationdate: ").append(toIndentedString(registrationdate)).append("\n");
    sb.append("    rank: ").append(toIndentedString(rank)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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



}
