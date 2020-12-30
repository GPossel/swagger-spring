package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

@NoArgsConstructor
@Validated
public class UserResponse {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("firstname")
    private String firstname = null;

    @JsonProperty("lastname")
    private String lastname = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("phone")
    private String phone = null;

    @JsonProperty("birthdate")
    private String birthdate = null;

    @JsonProperty("registrationdate")
    private String registrationdate = null;

    @JsonProperty("rank")
    private String rank = null;

    @JsonProperty("status")
    private String status = null;


    public UserResponse(User u) {
        this.id = u.getId();
        this.firstname = u.getFirstname();
        this.lastname = u.getLastname();
        this.email = u.getEmail();
        this.phone = u.getPhone();
        this.birthdate = u.getBirthdate().toString();
        this.registrationdate = u.getRegistrationdate().toString();
        this.rank = u.getRank().toString();
        this.status = u.getStatus().toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserResponse user = (UserResponse) o;
        return Objects.equals(this.id, user.id) &&
                Objects.equals(this.firstname, user.firstname) &&
                Objects.equals(this.lastname, user.lastname) &&
                Objects.equals(this.email, user.email) &&
                Objects.equals(this.phone, user.phone) &&
                Objects.equals(this.birthdate, user.birthdate) &&
                Objects.equals(this.registrationdate, user.registrationdate) &&
                Objects.equals(this.rank, user.rank) &&
                Objects.equals(this.status, user.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, email, phone, birthdate, registrationdate, rank, status);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserResponse {\n");
        sb.append("    firstname: ").append(toIndentedString(firstname)).append("\n");
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
