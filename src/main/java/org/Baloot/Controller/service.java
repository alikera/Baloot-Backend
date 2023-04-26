package org.Baloot.Controller;

import org.Baloot.Baloot;
import org.springframework.web.bind.annotation.*;

@RestController
public class service {
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password) {
        Baloot.getBaloot().userManager.login(username, password);
    }
}
