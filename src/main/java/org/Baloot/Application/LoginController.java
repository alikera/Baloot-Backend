package org.Baloot.Application;

import org.Baloot.Baloot;
import org.Baloot.Entities.Date;
import org.Baloot.Entities.User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/user")
public class LoginController {
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public User login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        return Baloot.getBaloot().userManager.login(username, password);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public User register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String email = body.get("email");
        String address = body.get("address");
        String date = body.get("birthDate");
        return Baloot.getBaloot().userManager.registerNewUser(username, password, email, address, date);
    }
}
