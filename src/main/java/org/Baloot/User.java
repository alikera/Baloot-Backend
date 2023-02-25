package org.Baloot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private String username;
    private String password;
    private String email;
    private String birthDate;
    private String address;
    private double credit;

    public User(@JsonProperty("username") String _username, @JsonProperty("password") String _password,
                @JsonProperty("email") String _email, @JsonProperty("birthDate") String _birthDate,
                @JsonProperty("address") String _address, @JsonProperty("credit") double _credit) {
        username = _username;
        password = _password;
        email = _email;
        birthDate = _birthDate;
        address = _address;
        credit = _credit;
    }

    public void print() {
        System.out.println(username + " " + password + " " + email + " " + birthDate + " " + address + " " + credit);
    }
}