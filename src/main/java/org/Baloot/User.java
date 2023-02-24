package org.Baloot;
public class User {
    private static String username;
    private static String password;

    private static String email;
    private static String birthDate;
    private static String address;

    private static double credit;

    public User(String _username, String _password, String _email, String _birthDate,
                String _address, double _credit) {
        username = _username;
        password = _password;
        email = _email;
        birthDate = _birthDate;

        address = _address;
        credit = _credit;
    }

    public void print() {
        System.out.println(username + password + email + birthDate + address + credit);
    }
}