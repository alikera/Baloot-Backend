package org.Baloot.Application;

import org.Baloot.Baloot;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.Provider;
import org.Baloot.Exception.ProviderNotFoundException;
import org.Baloot.Exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/provider")
public class ProviderController {
    @RequestMapping(value = "/{providerId}", method = RequestMethod.GET)
    public ResponseEntity<?> getCommodities(@PathVariable String providerId,
                                            @RequestParam (value = "username") String username)  {
        List<Commodity> commodities = Baloot.getBaloot().providerManager.getProvidersCommodities(providerId);
        Provider provider;
        try {
            provider = Baloot.getBaloot().getProviderById(Integer.valueOf(providerId));
        }
        catch (ProviderNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(408).body("Database Error");
        }
        System.out.println("jere");
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("commodities", commodities);
        responseMap.put("providerName", provider.getName());
        responseMap.put("providerDate", provider.getDate().year);
        responseMap.put("providerImage", provider.getImage());
        try {
            HashMap<Integer, Integer> userBuyList = Baloot.getBaloot().getUserByUsername(username).getBuyList();
            responseMap.put("cartCount", userBuyList.keySet().size());
            responseMap.put("buyList", userBuyList);
            return ResponseEntity.ok(responseMap);
        }
        catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(408).body("Database Error");
        }

    }

}
