package org.Baloot.Application;

import org.Baloot.Baloot;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.Date;
import org.Baloot.Entities.User;
import org.Baloot.Exception.IncorrectPasswordException;
import org.Baloot.Exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class HomeController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getCommodities(@RequestParam(value = "search") String search, @RequestParam(value = "option") String option) {

        System.out.println(search);
        System.out.println(option);
        List<Commodity> commodities = null;
        if(option.equals("category")) {
            commodities = Baloot.getBaloot().commodityManager.getCommoditiesByCategory(search);
            System.out.println(commodities.size());
        }
        else if (option.equals("name")) {
            commodities = Baloot.getBaloot().commodityManager.getCommoditiesByName(search);
        }
        return ResponseEntity.ok(commodities);
    }

}
