package org.Baloot;

import org.Baloot.Database.Database;
import org.Baloot.Entities.Comment;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.Provider;
import org.Baloot.Entities.User;
import org.Baloot.Exception.*;
import org.Baloot.Managers.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Baloot {
    private static Baloot baloot = null;

    private Database db;
    public UserManager userManager;
    public Baloot(Database _db) throws ProviderNotFoundException {
        db = _db;
        userManager = new UserManager(_db);
        for (Commodity commodity : db.getCommodities()) {
            addToProviderCommodityList(commodity);
        }
    }
    public void addProvider(Provider provider) {
        db.insertProvider(provider);
    }
    public void addCommodity(Commodity commodity) throws ProviderNotFoundException {
        addToProviderCommodityList(commodity);
        db.insertCommodity(commodity);
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

    public Provider findByProviderId(int providerId) throws ProviderNotFoundException {
        for (Provider provider : db.getProviders()) {
            if (Objects.equals(provider.getId(), providerId)) {
                return provider;
            }
        }
        throw new ProviderNotFoundException("Couldn't find provider with the given Id!");
    }

    private void addToProviderCommodityList(Commodity commodity) throws ProviderNotFoundException {
        for(Provider provider : db.getProviders()){
            if(Objects.equals(provider.getId(), commodity.getProviderId())){
                provider.addToCommodities(commodity);
                return;
            }
        }
        throw new ProviderNotFoundException("Couldn't find provider with the given Id!");
    }


    public void voteComment(String userId, String commodityId, String vote) throws UserNotFoundException, CommodityNotFoundException, InvalidVoteException {
        if (!Objects.equals(vote, "0") && !Objects.equals(vote, "1") && !Objects.equals(vote, "-1")) {
            throw new InvalidVoteException("Invalid Vote");
        }
        User user = db.findByUsername(userId);
        Commodity commodity = db.findByCommodityId(Integer.parseInt(commodityId));
        for (Comment comment : db.getComments()){
            if(comment.getCommodityId() == commodity.getId()){
                comment.voteComment(userId, Integer.parseInt(vote));
            }
        }
    }

    public void addCommodityToUserBuyList(String userId, String commodityId) throws CommodityNotFoundException, OutOfStockException, UserNotFoundException, CommodityExistenceException {
        Commodity commodityFound = db.findByCommodityId(Integer.parseInt(commodityId));
        if (commodityFound.getInStock() == 0) {
            throw new OutOfStockException("Commodity out of stock!");
        }
        User userFound = db.findByUsername(userId);
        userFound.addToBuyList(Integer.parseInt(commodityId));
        commodityFound.decreaseInStock();
    }

    public void removeCommodityFromUserBuyList(String userId, String commodityId) throws CommodityNotFoundException, UserNotFoundException, CommodityExistenceException {
        Commodity commodityFound = db.findByCommodityId(Integer.parseInt(commodityId));
        User userFound = db.findByUsername(userId);
        userFound.removeFromBuyList(Integer.parseInt(commodityId));
        commodityFound.increaseInStock();
    }

    public void rateCommodity(String userId, String commodityId, String rate) throws CommodityNotFoundException, UserNotFoundException, InvalidRatingException, NumberFormatException {
        Commodity commodityFound = db.findByCommodityId(Integer.parseInt(commodityId));
        User userFound = db.findByUsername(userId);
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
