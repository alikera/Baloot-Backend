package org.Baloot.Managers;

import org.Baloot.Database.Database;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.User;
import org.Baloot.Exception.*;
import org.springframework.http.ResponseEntity;

import javax.xml.crypto.Data;
import java.sql.SQLException;
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
            Database.insertUser(user);
        }
    }

    public HashMap<Commodity, Integer> getUserBuyList(String username) throws UserNotFoundException, CommodityNotFoundException, SQLException {
        User user = Database.findByUsername(username);
        HashMap<Integer, Integer> buyList = user.getBuyList();
        HashMap<Commodity, Integer> commodities = new HashMap<>();
        for (int commodityId: buyList.keySet()) {
            Commodity commodity = Database.findByCommodityId(commodityId);
            commodities.put(commodity, buyList.get(commodityId));
        }
        return commodities;
    }

    public HashMap<Commodity, Integer> getUserPurchasedList(String username) throws UserNotFoundException, CommodityNotFoundException, SQLException {
        User user = Database.findByUsername(username);
        List<Integer> purchasedListIds = user.getPurchasedList();
        List<Integer> purchasedCounts = user.getPurchasedCounts();
        HashMap<Commodity, Integer> commodities = new HashMap<>();
        for (int i = 0; i < purchasedListIds.size(); i++) {
            Commodity commodity = Database.findByCommodityId(purchasedListIds.get(i));
            commodities.put(commodity, purchasedCounts.get(i));
        }
        return commodities;
    }

    public void addCredit(String username, String credit) throws UserNotFoundException, NegativeAmountException, SQLException {
        double amount = Double.parseDouble(credit);
        if (amount <= 0) {
            throw new NegativeAmountException();
        }
        Database.increaseUserCredit(username, amount);
    }

    public void finalizePayment(String username, String discountCode, double discountValue, Map<Integer, Integer> commodityCounts) throws UserNotFoundException, NotEnoughCreditException, CommodityNotFoundException, SQLException {
        User user = Database.findByUsername(username);
        user.updateBuyListQuantities(commodityCounts);
        HashMap<Integer, Integer> buyList = user.getBuyList();
        Set<Integer> commoditiesId = buyList.keySet();
        double cost = 0;

        for (Integer id: commoditiesId) {
            Commodity commodity = Database.findByCommodityId(id);
            cost += (commodity.getPrice() * buyList.get(id));
        }
        user.moveBuyToPurchased(cost * (1 - discountValue), discountCode);
    }
    public void addCommodityToUserBuyList(String userId, String commodityId) throws CommodityNotFoundException, OutOfStockException, UserNotFoundException, CommodityExistenceException, SQLException {
        Commodity commodityFound = Database.findByCommodityId(Integer.parseInt(commodityId));
        if (commodityFound.getInStock() == 0) {
            throw new OutOfStockException("Commodity out of stock!");
        }
        User userFound = Database.findByUsername(userId);
        userFound.addToBuyList(Integer.parseInt(commodityId));
        commodityFound.decreaseInStock();
    }

    public void removeCommodityFromUserBuyList(String userId, String commodityId) throws CommodityNotFoundException, UserNotFoundException, CommodityExistenceException, SQLException {
        Commodity commodityFound = Database.findByCommodityId(Integer.parseInt(commodityId));
        User userFound = Database.findByUsername(userId);
        userFound.removeFromBuyList(Integer.parseInt(commodityId));
        commodityFound.increaseInStock();
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
