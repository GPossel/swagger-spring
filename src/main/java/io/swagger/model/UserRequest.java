package io.swagger.model;

import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@NoArgsConstructor
@Validated
public class UserRequest {
    public String firstname;
    public String lastname;
    public String email;
    public String password;
    public String rPassword;
    public String phone;
    public String birthdate;
    public User.RankEnum rank;


    public UserRequest(String firstname, String lastname, String email, String password, String rPassword, String phone, String birthdate, User.RankEnum rank) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.rPassword = rPassword;
        this.phone = phone;
        this.birthdate = birthdate;
        this.rank = rank;
    }


    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getrPassword() {
        return rPassword;
    }

    public String getPhone() {
        return phone;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public User.RankEnum getRank() {
        return rank;
    }

}
