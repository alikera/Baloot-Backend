package org.Baloot.Application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;
import org.Baloot.Baloot;
//import org.Baloot.Entities.Date;
import org.Baloot.Entities.User;
import org.Baloot.Exception.DuplicateEmailException;
import org.Baloot.Exception.DuplicateUsernameException;
import org.Baloot.Exception.IncorrectPasswordException;
import org.Baloot.Exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/auth")
public class LoginController {
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String userEmail = body.get("email");
        String password = body.get("password");
        System.out.println("here");
        try {
            String username = Baloot.getBaloot().userManager.login(userEmail, password);
            String token = generateJwtToken(username);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok().body(response);
        }
        catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        catch (IncorrectPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(408).body("Database Error");
        }
    }

    private String generateJwtToken(String username) {
        String SECRET_KEY = "baloot2023";
        // Create claims for JWT payload
        Claims claims = Jwts.claims();
        claims.setSubject(username);
        claims.setIssuedAt(new Date());

        // Set expiration time to 1 day from now
        Date expirationDate = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        claims.setExpiration(expirationDate);

        byte[] keyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY+SECRET_KEY+SECRET_KEY+SECRET_KEY+SECRET_KEY);
        Key key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());

        // Generate JWT token with signature
        return Jwts.builder()
                .setIssuer("baloot")
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .setIssuedAt(Date.from(Instant.now()))
                .claim("username", username)
                .signWith(key)
                .compact();
    }
    private static Key getKey() {
        String SECRET_KEY = "baloot2023";

        byte[] keyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY+SECRET_KEY+SECRET_KEY+SECRET_KEY+SECRET_KEY);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public static Claims parseJWT(String jwtString) {
        Key key = getKey();

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwtString)
                .getBody();
    }
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String email = body.get("email");
        String address = body.get("address");
        String date = body.get("birthDate");
        System.out.println(username+ password+email+ address+ date);
        try {
            Baloot.getBaloot().userManager.registerNewUser(username, password, email, address, date);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (DuplicateUsernameException e) {
            return ResponseEntity.status(410).body("Duplicate username");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(408).body("Database Error");
        } catch (DuplicateEmailException e) {
            return ResponseEntity.status(411).body("Duplicate email");
        }
    }
}
