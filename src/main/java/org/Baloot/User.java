package org.Baloot;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.Baloot.Exception.CommodityExistenceException;
import org.Baloot.Exception.NotEnoughCreditException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class User {

    private String username;
    private String password;
    private String email;
    private Date birthDate;
    private String address;
    private double credit;
    private Set<Integer> buyList = new HashSet<>();
    private List<Integer> purchasedList = new ArrayList<>();
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

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

    public Set<Integer> getBuyList() {
        return buyList;
    }

    public List<Integer> getPurchasedList() { return purchasedList; }

    public User(@JsonProperty("username") String _username, @JsonProperty("password") String _password,
                @JsonProperty("email") String _email, @JsonProperty("birthDate") String _birthDate,
                @JsonProperty("address") String _address, @JsonProperty("credit") double _credit) {
        username = _username;
        password = _password;
        email = _email;
        birthDate = new Date(_birthDate);
        address = _address;
        credit = _credit;
    }

    public void modifyFields(User user) {
        password = user.getPassword();
        email = user.getEmail();
        birthDate = user.getDate();
        address = user.getAddress();
        credit = user.getCredit();
    }
    public void addToBuyList(int commodityId) throws CommodityExistenceException {
        if(buyList.contains(commodityId)){
            throw new CommodityExistenceException("Commodity already exists in your BuyList!");
        }
        else {
            buyList.add(commodityId);
        }
    }
    public void removeFromBuyList(int commodityId) throws CommodityExistenceException {
        if (buyList.contains(commodityId)) {
            buyList.remove(commodityId);
        } else {
            throw new CommodityExistenceException("Commodity does not exists in your BuyList!");
        }
    }
    public void increaseCredit(double amount) {
        credit += amount;
    }

    public void moveBuyToPurchased(double cost) throws NotEnoughCreditException {
        if (credit < cost) {
            throw new NotEnoughCreditException("Not Enough Credit Exception!");
        }

        purchasedList.addAll(buyList);
        buyList.clear();
        credit -= cost;
    }

    public void print() {
        System.out.println(username + " " + password + " " + email + " " + birthDate + " " + address + " " + credit);
    }
}