package org.Baloot.Managers;

import org.Baloot.Database.Database;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.User;
import org.Baloot.Exception.*;
import org.springframework.http.ResponseEntity;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserManager {
    private User LoggedInUser;

    public User getLoggedInUser() {
        return LoggedInUser;
    }

    public void setLoggedInUser(User _loggedInUser) {
        LoggedInUser = _loggedInUser;
    }

    public UserManager(){
    }

    public void registerNewUser(String username, String password, String email, String address, String date) throws SQLException, DuplicateUsernameException{
        User newUser = new User(username, password, email, date, address, 0);
        try {
            addUser(newUser);
        }
        catch (InvalidUsernameException e){
        }
    }
    public void addUser(User user) throws InvalidUsernameException, DuplicateUsernameException, SQLException {
        Pattern pattern = Pattern.compile("[0-9a-zA-Z]+");
        Matcher matcher = pattern.matcher(user.getUsername());
        try {
            if (matcher.matches()) {
                User foundUser = Database.findByUsername(user.getUsername());
                throw new DuplicateUsernameException("Username already exists");
            } else {
                throw new InvalidUsernameException("Invalid Username!");
            }
        }
        catch (UserNotFoundException e){
            user.hashPassword();
            Database.insertUser(user);
        }
    }
    public HashMap<Commodity, Integer> getUserBuyList(String username) throws UserNotFoundException, CommodityNotFoundException, SQLException {
        return Database.getUserList(username, "BuyList");
    }

    public HashMap<Commodity, Integer> getUserPurchasedList(String username) throws UserNotFoundException, CommodityNotFoundException, SQLException {
        return Database.getUserList(username, "PurchasedList");
    }

    public void addCredit(String username, String credit) throws UserNotFoundException, NegativeAmountException, SQLException {
        double amount = Double.parseDouble(credit);
        if (amount <= 0) {
            throw new NegativeAmountException();
        }
        Database.updateUserCredit(username, amount);
    }

    public void finalizePayment(String username, String discountCode, double discountValue, Map<Integer, Integer> commodityCounts) throws UserNotFoundException, NotEnoughCreditException, CommodityNotFoundException, SQLException {
        User user = Database.findByUsername(username);
        HashMap<Commodity, Integer> buyList = Database.getUserList(username, "BuyList");
        double cost = calculateCost(buyList, discountValue);
        if (user.getCredit() < cost) {
            throw new NotEnoughCreditException("Not Enough Credit Exception!");
        }
        if (!discountCode.equals("")) {
            Database.insertToUsedCode(username, discountCode);
        }
        Database.updateUserCredit(username, -1 * cost);
        moveBuyListToPurchasedList(username, buyList);
    }
    private double calculateCost(HashMap<Commodity, Integer> buyList, double discountValue) {
        double cost = 0;
        for (Commodity commodity: buyList.keySet()) {
            cost += commodity.getPrice() * buyList.get(commodity);
        }
        return cost * (1 - discountValue);
    }
    private void moveBuyListToPurchasedList(String username, HashMap<Commodity, Integer> buyList) throws SQLException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        for (Commodity commodity: buyList.keySet()) {
            Database.insertToPurchasedList(username, commodity.getId(), buyList.get(commodity), timestamp);
            Database.removeFromBuyList(username, String.valueOf(commodity.getId()));
        }
    }
    public void addCommodityToUserBuyList(String userId, String commodityId) throws CommodityNotFoundException, OutOfStockException, UserNotFoundException, CommodityExistenceException, SQLException {
        Commodity commodityFound = Database.findByCommodityId(Integer.parseInt(commodityId));
        if (commodityFound.getInStock() == 0) {
            throw new OutOfStockException("Commodity out of stock!");
        }
        Database.insertToBuyList(userId, commodityId);
        Database.modifyInStock(commodityId, -1);
    }

    public void removeCommodityFromUserBuyList(String userId, String commodityId) throws CommodityNotFoundException, UserNotFoundException, CommodityExistenceException, SQLException {
        Database.removeFromBuyList(userId, commodityId);
        Database.modifyInStock(commodityId, 1);
    }

    public void login(String username, String password) throws UserNotFoundException, IncorrectPasswordException, SQLException {
        User user = Database.findByUsername(username);
        if (Objects.equals(password, user.getPassword())) {
            setLoggedInUser(user);
            System.out.println("True");
        }
        else {
            System.out.println("False");
            throw new IncorrectPasswordException("Password is not Correct");
        }
    }
}
