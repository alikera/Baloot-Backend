package org.Baloot.Application;


import org.Baloot.Baloot;
import org.Baloot.Database.Database;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.User;
import org.Baloot.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/user")

public class UserController {
    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserPage(@PathVariable String username) {
        try {
            User user = Baloot.getBaloot().getUserByUsername(username);
            return ResponseEntity.ok(user);
        }
        catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(408).body("Database Error");
        }
    }

    @RequestMapping(value = "/buyList/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserBuyList(@PathVariable String username) {
        try {
            HashMap<Commodity, Integer> commoditiesMap = Baloot.getBaloot().userManager.getUserBuyList(username);
            return ResponseEntity.ok(hashToList(commoditiesMap));
        }
        catch (UserNotFoundException | CommodityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(408).body("Database Error");
        }
    }
    @RequestMapping(value = "/buyList/{username}", method = RequestMethod.PUT)
    public ResponseEntity<?> modifyUserBuyList(@PathVariable String username,
                                            @RequestBody Map<String, String> body) {
        try {
            String commodityId = body.get("commodityId");
            int count = Integer.parseInt(body.get("count"));

            if(count == 1){
                Baloot.getBaloot().userManager.addCommodityToUserBuyList(username, commodityId);
            } else if (count == -1) {
                Baloot.getBaloot().userManager.removeCommodityFromUserBuyList(username, commodityId);
            }
            Commodity commodity = Baloot.getBaloot().getCommodityById(Integer.parseInt(commodityId));
            return ResponseEntity.ok(commodity.getInStock());
        }
        catch (UserNotFoundException | CommodityNotFoundException | CommodityExistenceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (OutOfStockException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(408).body("Database Error");
        }
    }


    @RequestMapping(value = "/purchasedList/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserPurchasedList(@PathVariable String username) {
        try {
            HashMap<Commodity, Integer> commoditiesMap = Baloot.getBaloot().userManager.getUserPurchasedList(username);
            return ResponseEntity.ok(hashToList(commoditiesMap));
        }
        catch (UserNotFoundException | CommodityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(408).body("Database Error");
        }
    }

    @RequestMapping(value = "/addCredit/{username}", method = RequestMethod.POST)
    public ResponseEntity<?> addUserCredit(@PathVariable String username,
                                                          @RequestParam (value = "credit") String credit) {
        try {
            Baloot.getBaloot().userManager.addCredit(username, credit);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NegativeAmountException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(408).body("Database Error");
        }
    }

    @RequestMapping(value = "/pay/{username}", method = RequestMethod.POST)
    public ResponseEntity<?> pay(@PathVariable String username,
                                 @RequestParam (value = "discountCode") String discountCode,
                                 @RequestParam (value = "discountValue") String discountValue,
                                 @RequestBody Map<String, List<String>> requestBody) {

        List<String> keys = requestBody.get("keys");
        List<String> values = requestBody.get("values");
        Map<Integer, Integer> commodityCounts = new HashMap<>();

        for(int i=0; i<keys.size(); i++) {
            Map<Integer, Integer> map = new HashMap<>();
            commodityCounts.put(Integer.parseInt(keys.get(i)), Integer.parseInt(values.get(i)));
        }
        try {
            Baloot.getBaloot().userManager.finalizePayment(username, discountCode, Double.parseDouble(discountValue), commodityCounts);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (UserNotFoundException | CommodityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NotEnoughCreditException e) {
            return ResponseEntity.badRequest().body("Not Enough Credit");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(408).body("Database Error");
        }
    }

    @RequestMapping(value = "/discount", method = RequestMethod.GET)
    public ResponseEntity<?> applyDiscount(@RequestParam (value = "code") String discountCode,
                                           @RequestParam (value = "username") String username) {
        try {
            if (Database.findDiscount(username, discountCode)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } else {
                double discountValue = Database.getDiscountFromCode(discountCode);
                return ResponseEntity.ok(discountValue);
            }
        } catch (DiscountCodeNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(408).body("Database Error");
        }
    }

    private List<Object> hashToList(HashMap<Commodity, Integer> commoditiesMap) {
        List<Object> commoditiesList = new ArrayList<>();
        for (Map.Entry<Commodity, Integer> entry : commoditiesMap.entrySet()) {
            Commodity commodity = entry.getKey();
            Integer quantity = entry.getValue();
            Map<String, Object> commodityInfo = new HashMap<>();
            commodityInfo.put("info", commodity);
            commodityInfo.put("quantity", quantity.hashCode());
            commoditiesList.add(commodityInfo);
        }
        return commoditiesList;
    }
}

