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
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.*;

public class CommodityManager {

    public CommodityManager(){
    }

    public void rateCommodity(String userId, String commodityId, String rate) throws SQLException, InvalidRatingException {
        if (Integer.parseInt(rate) < 0 || Integer.parseInt(rate) > 10) {
            throw new InvalidRatingException("Invalid Rating");
        }
        Database.insertRating(commodityId, userId, rate);
        List<String> ratings = Database.getRatings(commodityId);
        Database.updateRating(commodityId, calculateAvgRating(ratings));
    }
    public double calculateAvgRating(List<String> ratings) {
        double sum = 0.0;
        for (String rating : ratings) {
            sum += Double.parseDouble(rating);
        }
        return (ratings.size() == 0) ? 0 : sum / ratings.size();
    }

    public List<Commodity> getCommodities(String option, String search,
                                          String sortBy, boolean available) throws Exception {
        List<Commodity> commodities = new ArrayList<>();
        if(Objects.equals(sortBy, "")){
            sortBy = "cid";
        } else {
            System.out.println("X");
            int x = 0;
            x = x+ 1;
        }
        if(option.equals("category")) {
            commodities = Database.getCommodities(search, available ? 1 : 0, sortBy, "category", "cid");
        }
        else if (option.equals("name")) {
            commodities = Database.getCommodities(search, available ? 1 : 0, sortBy, "commodity", "cid");

        } else if (option.equals("provider")) {
            commodities = Database.getCommodities(search, available ? 1 : 0, sortBy, "provider", "pid");
        }
        return commodities;

    }
    public List<Commodity> getSuggestedCommodities(Commodity currentCommodity) throws Exception {
        return Database.getSuggestedCommodities(currentCommodity.getId());
    }
}
