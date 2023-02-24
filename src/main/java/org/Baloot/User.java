package org.Baloot;
public class User {
    private String username;
    private String password;

    private String email;
    private String birthDate;
    private String address;

    private double credit;

    public User(String _username, String _password, String _email, String _birthDate,
                String _address, double _credit) {
        username = _username;
        password = _password;
        email = _email;
        birthDate = _birthDate;

        address = _address;
        credit = _credit;
    }
}