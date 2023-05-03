package org.Baloot.Application;

import org.Baloot.Baloot;
import org.Baloot.Entities.Commodity;
import org.Baloot.Exception.ProviderNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                                            @RequestParam(value = "page", defaultValue = "0") int page) {

//        System.out.println(search);
//        System.out.println(option);
//        System.out.println(available);
//        System.out.println(page);
//        System.out.println(sortBy);
//        System.out.println("\n");

        List<Commodity> commodities = Baloot.getBaloot().getCommodities();
        if(option.equals("category")) {
            commodities = Baloot.getBaloot().commodityManager.getCommoditiesByCategory(search);
            System.out.println(commodities.size());
        }
        else if (option.equals("name")) {
            commodities = Baloot.getBaloot().commodityManager.getCommoditiesByName(search);
        } else if (option.equals("provider")) {
            try {
                commodities = Baloot.getBaloot().commodityManager.getCommoditiesByProvider(search);
            }catch (ProviderNotFoundException e){

            }
        }
        if(available){
            commodities = Baloot.getBaloot().commodityManager.getAvailableCommodities(commodities);
        }
        if (Objects.equals(sortBy, "name")) {
            Baloot.getBaloot().commodityManager.getSortedCommoditiesByName(commodities);
        } else if (Objects.equals(sortBy, "price")) {
            Baloot.getBaloot().commodityManager.getSortedCommoditiesByPrice(commodities);
        }
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

        return ResponseEntity.ok(responseMap);
    }

}
