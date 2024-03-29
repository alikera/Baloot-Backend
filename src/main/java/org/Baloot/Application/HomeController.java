package org.Baloot.Application;

import org.Baloot.Baloot;
import org.Baloot.Database.Database;
import org.Baloot.Entities.Comment;
import org.Baloot.Entities.Commodity;
import org.Baloot.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class HomeController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getCommodities(@RequestParam(value = "search") String search, @RequestParam(value = "option") String option,
                                            @RequestParam(value = "available") Boolean available, @RequestParam(value = "sort") String sortBy,
                                            @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "username") String username) {

        try {
            System.out.println(sortBy);
            List<Commodity> commodities = Baloot.getBaloot().commodityManager.getCommodities(option,search,sortBy,available);
            page = page -1;
            int size = 12;
            int totalItems = commodities.size();
            int totalPages = (int) Math.ceil((double) totalItems / size);

            if (page < 0 || page >= totalPages) {
                return ResponseEntity.badRequest().build();
            }
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, totalItems);

            commodities = commodities.subList(startIndex, endIndex);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("commodities", commodities);
            responseMap.put("totalPages", totalPages);

            HashMap<Integer, Integer> userBuylist = castToIntegerHashMap(Database.getUserList(username, "BuyList"));
            int cartCounter = userBuylist.keySet().size();
            responseMap.put("cartCount", cartCounter);
            responseMap.put("buylist", userBuylist);
            System.out.println("hereeeeeee:" + userBuylist.size());
            System.out.println("username:" + username);

            return ResponseEntity.ok(responseMap);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(408).body("Database Error");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<Integer, Integer> castToIntegerHashMap(HashMap<Commodity, Integer> originalHashmap){
        HashMap<Integer, Integer> newHashMap = new HashMap<>();
        for(Commodity commodity: originalHashmap.keySet()){
            newHashMap.put(commodity.getId(),originalHashmap.get(commodity));
        }
        return newHashMap;
    }
    @RequestMapping(value = "/commodity/{id}/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> getCommodity(@PathVariable (value = "id") String id,
                                          @PathVariable (value = "username") String username) {
        try {
            Commodity commodity = Database.findByCommodityId(Integer.parseInt(id));
            for (String cc:commodity.getCategories()){
                System.out.println(cc);
            }
            List<Comment> comments = Database.getComments(Integer.parseInt(id));
            String providerName = Database.findByProviderId(commodity.getProviderId()).getName();
            List<Commodity> suggestedCommodities = Baloot.getBaloot().commodityManager.getSuggestedCommodities(commodity);
            HashMap<String, Object> response = new HashMap<>();
            response.put("info", commodity);
            response.put("comments", comments);
            response.put("providerName", providerName);
            response.put("suggested", suggestedCommodities);
            HashMap<Integer, Integer> userBuyList = castToIntegerHashMap(Database.getUserList(username, "BuyList"));
            response.put("cartCount", userBuyList.keySet().size());
            response.put("buyList", userBuyList);
            return ResponseEntity.ok(response);
        } catch (CommodityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(408).body("Database Error");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @RequestMapping(value = "/rate/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> rateCommodity(@PathVariable(value = "id") String id,
                                         @RequestBody Map<String, String> body) {
        try {
            String username = body.get("username");
            String rate = body.get("rate");
            Baloot.getBaloot().commodityManager.rateCommodity(username, id, rate);
            Commodity commodity = Baloot.getBaloot().getCommodityById(Integer.parseInt(id));
            double rating = commodity.getRating();
            int count = commodity.getCountOfRatings();
            HashMap<String, String> response = new HashMap<>();
            response.put("rating", Double.toString(rating));
            response.put("count", Integer.toString(count));
            return ResponseEntity.ok(response);
        } catch ( CommodityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidRatingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(408).body("Database Error");
        }
    }

    @RequestMapping(value = "/comment/post/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> postComment(@PathVariable(value = "id") String id,
                                         @RequestBody Map<String, String> body) {

        String username = body.get("username");
        String text = body.get("text");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currDate = LocalDate.now().format(formatter);
        Comment comment = new Comment(username, Integer.parseInt(id), text, currDate);
        try {
            Database.insertComment(comment);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(408).body("Database Error");
        }
        return ResponseEntity.ok(comment);
    }

    @RequestMapping(value = "/comment/vote/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> vote(@PathVariable (value = "id") String id,
                                           @RequestBody Map<String, String> body) {
        try {
            String username = body.get("username");
            String commentId = body.get("commentId");
            String vote = body.get("vote");
//            int status = Baloot.getBaloot().commentManager.voteComment(username, id, commentId, vote);
            int status = Database.insertVoteToComment(username,commentId,vote);
            return ResponseEntity.ok(status);
        } catch (InvalidVoteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(408).body("Database Error");
        }
    }
}
