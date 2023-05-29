package org.Baloot.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.Baloot.Database.Database;
import org.Baloot.Exception.CommodityExistenceException;
import org.Baloot.Exception.NotEnoughCreditException;

import java.sql.SQLException;
import java.util.*;

public class User {
//    private static int count = 0;

//    private Integer id;
    private String username;
    private String password;
    private String email;
    private Date birthDate;
    private String address;
    private double credit;
    HashMap<Integer, Integer> buyList = new HashMap<>();
    private Set<String> usedDiscountCodes = new HashSet<>();
    private List<Integer> purchasedList = new ArrayList<>();
    private List<Integer> purchasedCounts = new ArrayList<>();

    public User(@JsonProperty("username") String _username, @JsonProperty("password") String _password,
                @JsonProperty("email") String _email, @JsonProperty("birthDate") String _birthDate,
                @JsonProperty("address") String _address, @JsonProperty("credit") double _credit) {
        username = _username;
        password = _password;
        email = _email;
        birthDate = new Date(_birthDate);
        address = _address;
        credit = _credit;
//        count++;
//        id = count;
    }
//    public Integer getId() {
//        return id;
//    }

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

    public HashMap<Integer, Integer> getBuyList() {
        return buyList;
    }

    public List<Integer> getPurchasedList() { return purchasedList; }
    public List<Integer> getPurchasedCounts() { return purchasedCounts; }

    public HashMap<String, String> getAttributes() {
        HashMap<String, String> attributes = new HashMap<>();

//        attributes.put("id", id.toString());
        attributes.put("username", username);
        attributes.put("password", password);
        attributes.put("email", email);
        attributes.put("birthDate", birthDate.getAsSqlDate().toString());
        attributes.put("address", address);
        attributes.put("credit", String.valueOf(credit));

        return attributes;
    }
    public void modifyFields(User user) {
        password = user.getPassword();
        email = user.getEmail();
        birthDate = user.getDate();
        address = user.getAddress();
        credit = user.getCredit();
    }
    public void addToBuyList(int commodityId) {
        if(buyList.containsKey(commodityId)){
            int value = buyList.get(commodityId);
            buyList.put(commodityId, value + 1);
        }
        else {
            buyList.put(commodityId, 1);
        }
    }
    public void removeFromBuyList(int commodityId) throws CommodityExistenceException {
        if (buyList.containsKey(commodityId)) {
            int value = buyList.get(commodityId);
            if (value == 1){
                buyList.remove(commodityId);
            }
            else {
                buyList.put(commodityId, value - 1);
            }
        } else {
            throw new CommodityExistenceException("Commodity does not exists in your BuyList!");
        }
    }
    public void increaseCredit(double amount) {
        credit += amount;
    }

    public void moveBuyToPurchased(double cost, String discountCode) throws NotEnoughCreditException, SQLException {
        if (credit < cost) {
            throw new NotEnoughCreditException("Not Enough Credit Exception!");
        }
        for (Integer key: buyList.keySet()) {
            purchasedList.add(key);
            purchasedCounts.add(buyList.get(key));
        }

        // DELETE USER BUY LIST

        buyList.clear();
        credit -= cost;
        if (!Objects.equals(discountCode, "")) {
            usedDiscountCodes.add(discountCode);
            Database.insertToUsedCode(username, discountCode);
        }
    }

    public void updateBuyListQuantities(Map<Integer, Integer> commodityCounts) {
        if (commodityCounts == null)
            return;
        for (Map.Entry<Integer, Integer> entry : commodityCounts.entrySet()) {
            Integer id = entry.getKey();
            Integer quantity = entry.getValue();
            buyList.put(id, quantity);
        }
    }

    public boolean isDiscountCodeUsed(String discountCode) {
        return usedDiscountCodes.contains(discountCode);
    }

    public void print() {
        System.out.println(username + " " + password + " " + email + " " + birthDate + " " + address + " " + credit);
    }
}