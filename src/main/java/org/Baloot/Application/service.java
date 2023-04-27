package org.Baloot.Application;

import org.Baloot.Baloot;
import org.Baloot.Database.Database;
import org.Baloot.Entities.User;
import org.Baloot.Exception.UserNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class service {
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public User login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        return Baloot.getBaloot().userManager.login(username, password);
    }
}
