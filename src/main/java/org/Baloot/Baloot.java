package org.Baloot;

import org.Baloot.Database.Database;
import org.Baloot.Entities.Comment;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.Provider;
import org.Baloot.Entities.User;
import org.Baloot.Exception.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Baloot {
    private Database db;
    public Baloot(Database _db) throws ProviderNotFoundException {
        db = _db;
        for (Commodity commodity : db.getCommodities()) {
            addToProviderCommodityList(commodity);
        }
    }

    public Commodity findByCommodityId(int commodityId) throws CommodityNotFoundException {
        for (Commodity commodity : db.getCommodities()) {
            if (commodity.getId() == commodityId) {
                return commodity;
            }
        }
        throw new CommodityNotFoundException("Couldn't find commodity with the given Id!");
    }

    public List<Commodity> getCommoditiesByPriceRange(String _startPrice, String _endPrice) throws NumberFormatException {
        double startPrice = Double.parseDouble(_startPrice);
        double endPrice = Double.parseDouble(_endPrice);

        List<Commodity> filteredCommodities = new ArrayList<>();
        for (Commodity commodity : db.getCommodities()) {
            if (commodity.getPrice() >= startPrice && commodity.getPrice() <= endPrice) {
                filteredCommodities.add(commodity);
            }
        }
        return filteredCommodities;
    }
    public List<Commodity> getCommoditiesByCategory(String category) {
        List<Commodity> filteredCommodities = new ArrayList<>();
        for (Commodity commodity : db.getCommodities()) {
            if (commodity.isInCategoryGiven(category)) {
                filteredCommodities.add(commodity);
            }
        }
        return filteredCommodities;
    }

    public List<Comment> getCommentsByCommodityId(int commodityId){
        List<Comment> filteredComments = new ArrayList<>();
        for (Comment comment : db.getComments()) {
            if (comment.getCommodityId() == commodityId) {
                filteredComments.add(comment);
            }
        }
        return filteredComments;
    }
    public User findByUsername(String username) throws UserNotFoundException {
        for (User user : db.getUsers()) {
            if (Objects.equals(user.getUsername(), username)) {
                return user;
            }
        }
        throw new UserNotFoundException("Couldn't find user with the given Username!");
    }

    public String findUsernameByUserEmail(String email) throws UserNotFoundException {
        for (User user : db.getUsers()) {
            if (Objects.equals(user.getEmail(), email)) {
                return user.getUsername();
            }
        }
        throw new UserNotFoundException("Couldn't find user with the given Username!");
    }
    public Provider findByProviderId(int providerId) throws ProviderNotFoundException {
        for (Provider provider : db.getProviders()) {
            if (Objects.equals(provider.getId(), providerId)) {
                return provider;
            }
        }
        throw new ProviderNotFoundException("Couldn't find provider with the given Id!");
    }

    private void addToProviderCommodityList(Commodity commodity) {
        for(Provider provider : db.getProviders()){
            if(Objects.equals(provider.getId(), commodity.getProviderId())){
                provider.addToCommodities(commodity);
            }
        }
    }

    public void addCredit(String username, String credit) throws UserNotFoundException, NegativeAmountException {
        double amount = Double.parseDouble(credit);

        User user = findByUsername(username);
        if (amount <= 0) {
            throw new NegativeAmountException();
        }
        user.increaseCredit(amount);
    }
    public void finalizePayment(String username) throws UserNotFoundException, NotEnoughCreditException, CommodityNotFoundException {
        User user = findByUsername(username);
        Set<Integer> commoditiesId = user.getBuyList();
        double cost = 0;

        for (Integer id: commoditiesId) {
            Commodity commodity = findByCommodityId(id);
            cost += commodity.getPrice();
        }
        user.moveBuyToPurchased(cost);
    }

    public void voteComment(String userId, String commodityId, String vote) throws UserNotFoundException, CommodityNotFoundException, InvalidVoteException {
        if (!Objects.equals(vote, "0") && !Objects.equals(vote, "1") && !Objects.equals(vote, "-1")) {
            throw new InvalidVoteException("Invalid Vote");
        }
        User user = findByUsername(userId);
        Commodity commodity = findByCommodityId(Integer.parseInt(commodityId));
        for (Comment comment : db.getComments()){
            if(comment.getCommodityId() == commodity.getId()){
                comment.voteComment(userId, Integer.parseInt(vote));
            }
        }
    }

    public void addCommodityToUserBuyList(String userId, String commodityId) throws CommodityNotFoundException, OutOfStockException, UserNotFoundException, CommodityExistenceException {
        Commodity commodityFound = findByCommodityId(Integer.parseInt(commodityId));
        if (commodityFound.getInStock() == 0) {
            throw new OutOfStockException("Commodity out of stock!");
        }
        User userFound = findByUsername(userId);
        userFound.addToBuyList(Integer.parseInt(commodityId));
        commodityFound.decreaseInStock();
    }

    public void removeCommodityFromUserBuyList(String userId, String commodityId) throws CommodityNotFoundException, UserNotFoundException, CommodityExistenceException {
        Commodity commodityFound = findByCommodityId(Integer.parseInt(commodityId));
        User userFound = findByUsername(userId);
        userFound.removeFromBuyList(Integer.parseInt(commodityId));
        commodityFound.increaseInStock();
    }

    public void rateCommodity(String userId, String commodityId, String rate) throws CommodityNotFoundException, UserNotFoundException, InvalidRatingException, NumberFormatException {
        Commodity commodityFound = findByCommodityId(Integer.parseInt(commodityId));
        User userFound = findByUsername(userId);
        commodityFound.rateCommodity(userId, Integer.parseInt(rate));
    }
    public List<User> getUsers() {
        return db.getUsers();
    }
    public List<Provider> getProviders() {
        return db.getProviders();
    }
    public List<Commodity> getCommodities() {
        return db.getCommodities();
    }


}
