package org.Baloot.Application;

import org.Baloot.Baloot;
import org.Baloot.Entities.Date;
import org.Baloot.Entities.User;
import org.Baloot.Exception.DuplicateUsernameException;
import org.Baloot.Exception.IncorrectPasswordException;
import org.Baloot.Exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/auth")
public class LoginController {
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        System.out.println(username);
        try {
            Baloot.getBaloot().userManager.login(username, password);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        catch (IncorrectPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String email = body.get("email");
        String address = body.get("address");
        String date = body.get("birthDate");
        System.out.println(username);
        try {
            Baloot.getBaloot().userManager.registerNewUser(username, password, email, address, date);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (DuplicateUsernameException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
