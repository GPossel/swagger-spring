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
}
