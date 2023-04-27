package org.Baloot.Controller;

import org.Baloot.Baloot;
import org.Baloot.Database.Database;
import org.Baloot.Entities.User;
import org.Baloot.Exception.UserNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class service {
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public User login(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password) throws UserNotFoundException {
        return Database.findByUsername(username);
    }
}
