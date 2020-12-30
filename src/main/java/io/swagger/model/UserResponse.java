package io.swagger.model;

import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

@NoArgsConstructor
@Validated
public class UserResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String birthdate;
    private String registrationdate;
    private String rank;
    private String status;

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
        sb.append("class User {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    firstname: ").append(toIndentedString(firstname)).append("\n");
        sb.append("    lastname: ").append(toIndentedString(lastname)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
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
