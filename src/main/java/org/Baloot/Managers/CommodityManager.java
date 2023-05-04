package org.Baloot.Managers;

//import kotlin.Pair;
import org.Baloot.Baloot;
import org.Baloot.Database.Database;
import org.Baloot.Entities.Comment;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.Provider;
import org.Baloot.Entities.User;
import org.Baloot.Exception.CommodityNotFoundException;
import org.Baloot.Exception.InvalidRatingException;
import org.Baloot.Exception.ProviderNotFoundException;
import org.Baloot.Exception.UserNotFoundException;

import java.util.*;

public class CommodityManager {
    public CommodityManager(){
    }
    public void addCommodity(Commodity commodity) throws ProviderNotFoundException {
        Database.insertCommodity(commodity);
    }
    public List<Commodity> getCommoditiesByPriceRange(String _startPrice, String _endPrice) throws NumberFormatException {
        double startPrice = Double.parseDouble(_startPrice);
        double endPrice = Double.parseDouble(_endPrice);

        List<Commodity> filteredCommodities = new ArrayList<>();
        for (Commodity commodity : Database.getCommodities()) {
            if (commodity.getPrice() >= startPrice && commodity.getPrice() <= endPrice) {
                filteredCommodities.add(commodity);
            }
        }
        return filteredCommodities;
    }
    public List<Commodity> getCommoditiesByCategory(String category) {
        List<Commodity> filteredCommodities = new ArrayList<>();
        for (Commodity commodity : Database.getCommodities()) {
            if (commodity.isInCategoryGiven(category)) {
                filteredCommodities.add(commodity);
            }
        }
        return filteredCommodities;
    }
    public List<Commodity> getCommoditiesByName(String name) {
        List<Commodity> filteredCommodities = new ArrayList<>();
        for (Commodity commodity : Database.getCommodities()) {
            if (commodity.getName().toLowerCase().startsWith(name.toLowerCase())) {
                filteredCommodities.add(commodity);
            }
        }
        return filteredCommodities;
    }

    public List<Commodity> getCommoditiesByProvider(String name) throws ProviderNotFoundException {
        List<Commodity> filteredCommodities = new ArrayList<>();
        for (Commodity commodity : Database.getCommodities()) {
            if (Baloot.getBaloot().getProviderById(commodity.getProviderId()).getName().equals(name)) {
                filteredCommodities.add(commodity);
            }
        }
        return filteredCommodities;
    }
    public void rateCommodity(String userId, String commodityId, String rate) throws CommodityNotFoundException, UserNotFoundException, InvalidRatingException, NumberFormatException {
        Commodity commodityFound = Database.findByCommodityId(Integer.parseInt(commodityId));
        User userFound = Database.findByUsername(userId);
        commodityFound.rateCommodity(userId, Integer.parseInt(rate));
    }
    public void getSortedCommoditiesByRating(List<Commodity> _commodities){
        _commodities.sort(Comparator.comparingDouble(Commodity::getRating));
    }
    public void getSortedCommoditiesByName(List<Commodity> _commodities){
        _commodities.sort(Comparator.comparing(Commodity::getName));
    }
    public void getSortedCommoditiesByPrice(List<Commodity> _commodities){
        _commodities.sort(Comparator.comparing(Commodity::getPrice));
    }
    public List<Commodity> getAvailableCommodities(List<Commodity> _commodities){
        List<Commodity> filteredCommodities = new ArrayList<>();
        for(Commodity commodity : _commodities){
            if(commodity.getInStock() > 0){
                filteredCommodities.add(commodity);
            }
        }
        return filteredCommodities;
    }
//
//    public List<Commodity> getSuggestedCommodities(Commodity currentCommodity ,Set<String> categories){
//        List<Pair<Commodity, Double>> weightedCommodities = new ArrayList<>();
//        for (Commodity commodity : Database.getCommodities()) {
//            if(currentCommodity.getId() != commodity.getId()) {
//                double score = calculateScoreForSuggestingCommodities(commodity, categories);
//                weightedCommodities.add(new Pair<>(commodity, score));
//            }
//        }
//        weightedCommodities.sort(Comparator.comparing(p -> -p.getSecond()));
//        List<Commodity> filteredCommodities = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            filteredCommodities.add(weightedCommodities.get(i).getFirst());
//        }
//
//        return filteredCommodities;
//    }
    private double calculateScoreForSuggestingCommodities(Commodity commodity, Set<String> categories){
        double score = commodity.getRating();
        if(commodity.getCategories().equals(categories)){
                score += 11;
        }
        return score;
    }

    public List<Comment> getCommentsOfCommodity(int id) {
        List<Comment> comments = Database.getComments();
        List<Comment> commodityComments = new ArrayList<>();
        for (Comment comment: comments) {
            if (id == comment.getCommodityId()) {
                commodityComments.add(comment);
            }
        }
        return commodityComments;
    }
}
