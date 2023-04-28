package org.Baloot.Application;


import org.Baloot.Baloot;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.User;
import org.Baloot.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/user")
public class UserController {
    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserPage(@PathVariable String username) {
        try {
            User user = Baloot.getBaloot().getUserByUsername(username);
            return ResponseEntity.ok(user);
        }
        catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping(value = "/buyList/{username}", method = RequestMethod.GET)
    public ResponseEntity<List<Commodity>> getUserBuyList(@PathVariable String username) {
        try {
            List<Commodity> commodities= Baloot.getBaloot().userManager.getUserBuylist(username);
            return ResponseEntity.ok(commodities);
        }
        catch (UserNotFoundException | CommodityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping(value = "/purchasedList/{username}", method = RequestMethod.GET)
    public ResponseEntity<List<Commodity>> getUserPurchasedList(@PathVariable String username) {
//        User user = Baloot.getBaloot().getUserByUsername("amir");
//        Baloot.getBaloot().userManager.addCredit("amir", "40000");
//        user.addToBuyList(1);
//        user.addToBuyList(2);
//        Baloot.getBaloot().userManager.finalizePayment("amir", "", 0);
//        user.addToBuyList(3);
//        user.addToBuyList(4);
//        user.addToBuyList(5);
        try {
            List<Commodity> commodities= Baloot.getBaloot().userManager.getUserPurchasedList(username);
            return ResponseEntity.ok(commodities);
        }
        catch (UserNotFoundException | CommodityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
        }
    }

    @RequestMapping(value = "/pay/{username}", method = RequestMethod.POST)
    public ResponseEntity<?> pay(@PathVariable String username) {
        System.out.println("HERE");
        try {
            Baloot.getBaloot().userManager.finalizePayment(username, "", 0);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (UserNotFoundException | CommodityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (NotEnoughCreditException e) {
            return ResponseEntity.badRequest().body("Not Enough Credit");
        }
    }
}