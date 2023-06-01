package org.Baloot.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.Baloot.Database.Database;
import org.Baloot.Exception.CommodityExistenceException;
import org.Baloot.Exception.NotEnoughCreditException;
import java.sql.SQLException;
import java.util.*;
import org.mindrot.jbcrypt.BCrypt;

public class User {
    private String username;
    private String password;
    private String salt = "";
    private String email;
    private Date birthDate;
    private String address;
    private double credit;

    public User(@JsonProperty("username") String _username, @JsonProperty("password") String _password,
                @JsonProperty("salt") String _salt, @JsonProperty("email") String _email,
                @JsonProperty("birthDate") String _birthDate, @JsonProperty("address") String _address,
                @JsonProperty("credit") double _credit) {
        username = _username;
        password = _password;
        salt = _salt;
        email = _email;
        birthDate = new Date(_birthDate);
        address = _address;
        credit = _credit;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public String getSalt() { return salt; }
    public String getEmail() {
        return email;
    }
    public String getBirthDate() {
        return birthDate.getAsString();
    }
    public Date getDate(){
        return birthDate;
    }
    public String getAddress() {
        return address;
    }
    public double getCredit() {
        return credit;
    }

    public HashMap<String, String> getAttributes() {
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("username", username);
        attributes.put("password", password);
        attributes.put("email", email);
        attributes.put("birthDate", birthDate.getAsSqlDate().toString());
        attributes.put("address", address);
        attributes.put("credit", String.valueOf(credit));
        attributes.put("salt", salt);

        return attributes;
    }

    public void hashPassword() {
        salt = BCrypt.gensalt();
        password = BCrypt.hashpw(password, salt);
    }
}