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
@RequestMapping("/api/provider")
public class ProviderController {
    @RequestMapping(value = "/{providerId}", method = RequestMethod.GET)
    public ResponseEntity<?> getCommodities(@PathVariable String providerId) {

        List<Commodity> commodities = Baloot.getBaloot().providerManager.getProvidersCommodities(providerId);

        return ResponseEntity.ok(commodities);
    }

}
