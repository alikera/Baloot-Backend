package org.Baloot.Application;

import org.Baloot.Baloot;
import org.Baloot.Entities.Commodity;
import org.Baloot.Entities.Provider;
import org.Baloot.Exception.ProviderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/provider")
public class ProviderController {
    @RequestMapping(value = "/{providerId}", method = RequestMethod.GET)
    public ResponseEntity<?> getCommodities(@PathVariable String providerId)  {
//        System.out.println(1);
        List<Commodity> commodities = Baloot.getBaloot().providerManager.getProvidersCommodities(providerId);
//        System.out.println(commodities.size());
        Provider provider;
        try {
            provider = Baloot.getBaloot().getProviderById(Integer.valueOf(providerId));
        }
        catch (ProviderNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("commodities", commodities);
        responseMap.put("providerName", provider.getName());
        responseMap.put("providerDate", provider.getDate().year);
        responseMap.put("providerImage", provider.getImage());

        return ResponseEntity.ok(responseMap);
    }

}
