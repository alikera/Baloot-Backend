package org.Baloot.Managers;

import org.Baloot.Database.Database;
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
            if (Objects.equals(commodity.getName(), name)) {
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
}
