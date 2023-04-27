package org.Baloot.Managers;

import org.Baloot.Database.Database;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.User;
import org.Baloot.Exception.*;

import javax.xml.crypto.Data;
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

    public void registerNewUser(String username, String password, String email, String address, String date){
        User newUser = new User(username, password, email, date, address, 0);
        try {
            addUser(newUser);
        }
        catch (InvalidUsernameException e){

        }
    }
    public void addUser(User user) throws InvalidUsernameException {
        Pattern pattern = Pattern.compile("[0-9a-zA-Z]+");
        Matcher matcher = pattern.matcher(user.getUsername());
        try {
            if (matcher.matches()) {
                User foundUser = Database.findByUsername(user.getUsername());
                foundUser.modifyFields(user);
            } else {
                throw new InvalidUsernameException("Invalid Username!");
            }
        }
        catch (UserNotFoundException e){
            Database.insertUser(user);
        }
    }

    public List<Commodity> getUserBuylist(String username) throws UserNotFoundException, CommodityNotFoundException {
        User user = Database.findByUsername(username);
        Set<Integer> buyListIds = user.getBuyList();
        List<Commodity> commodities = new ArrayList<>();
        for (int commodityId : buyListIds) {
            Commodity commodity = Database.findByCommodityId(commodityId);
            commodities.add(commodity);
        }
        return commodities;
    }

    public void addCredit(String username, String credit) throws UserNotFoundException, NegativeAmountException {
        double amount = Double.parseDouble(credit);

        User user = Database.findByUsername(username);
        if (amount <= 0) {
            throw new NegativeAmountException();
        }
        user.increaseCredit(amount);
    }

    public void finalizePayment(String username, String discountCode, double discountValue) throws UserNotFoundException, NotEnoughCreditException, CommodityNotFoundException {
        User user = Database.findByUsername(username);
        Set<Integer> commoditiesId = user.getBuyList();
        double cost = 0;

        for (Integer id: commoditiesId) {
            Commodity commodity = Database.findByCommodityId(id);
            cost += commodity.getPrice();
        }
        user.moveBuyToPurchased(cost * (1 - discountValue), discountCode);
    }
    public void addCommodityToUserBuyList(String userId, String commodityId) throws CommodityNotFoundException, OutOfStockException, UserNotFoundException, CommodityExistenceException {
        Commodity commodityFound = Database.findByCommodityId(Integer.parseInt(commodityId));
        if (commodityFound.getInStock() == 0) {
            throw new OutOfStockException("Commodity out of stock!");
        }
        User userFound = Database.findByUsername(userId);
        userFound.addToBuyList(Integer.parseInt(commodityId));
        commodityFound.decreaseInStock();
    }

    public void removeCommodityFromUserBuyList(String userId, String commodityId) throws CommodityNotFoundException, UserNotFoundException, CommodityExistenceException {
        Commodity commodityFound = Database.findByCommodityId(Integer.parseInt(commodityId));
        User userFound = Database.findByUsername(userId);
        userFound.removeFromBuyList(Integer.parseInt(commodityId));
        commodityFound.increaseInStock();
    }

    public User login(String username, String password) {
        System.out.println(username + " " + password);
        try {
            User user = Database.findByUsername(username);
            if (Objects.equals(password, user.getPassword())) {
                System.out.println("True");
                return user;
            }
            else {
                System.out.println("False");
            }
        }
        catch (UserNotFoundException e) {
            System.out.println("Not Found");
        }
        return null;
    }
}
