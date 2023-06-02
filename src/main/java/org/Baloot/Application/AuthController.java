package org.Baloot.Application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;
import org.Baloot.Baloot;
import org.Baloot.Exception.DuplicateEmailException;
import org.Baloot.Exception.DuplicateUsernameException;
import org.Baloot.Exception.IncorrectPasswordException;
import org.Baloot.Exception.UserNotFoundException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/auth")
public class AuthController {
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

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String email = body.get("email");
        String address = body.get("address");
        String date = body.get("birthDate");
        System.out.println(username + password + email + address + date);
        try {
            Baloot.getBaloot().userManager.registerNewUser(username, password, email, address, date);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (DuplicateUsernameException e) {
            return ResponseEntity.status(410).body("Duplicate username");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(408).body("Database Error");
        } catch (DuplicateEmailException e) {
            return ResponseEntity.status(411).body("Duplicate email");
        }
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    public ResponseEntity<?> callback(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        System.out.println("code is:" + code);
        String clientID = "b8dba66689e0a75208f0";
        String clientSecret = "b80d957e19f83acfb5a6cf9aa536bacf3453d5d3";

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost("https://github.com/login/oauth/access_token?client_id=" + clientID +
                    "&client_secret=" + clientSecret + "&code=" + code);

            post.setHeader("Accept", "application/json");
            HttpResponse response = client.execute(post);

            // Handle the response
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                System.out.println("RESPONSE:");
                System.out.println(responseString);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(responseString);
                String accessToken = rootNode.get("access_token").asText();
                System.out.println("Access token: " + accessToken);
                HashMap<String, String> returnRes = new HashMap<>();
                returnRes.put("Token", accessToken);
                return ResponseEntity.status(200).body(returnRes);
            } else {
                System.out.println("Error: " + response.getStatusLine().getStatusCode());
                System.out.println(response.getEntity().getContent().toString());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}


//        try {
//            Baloot.getBaloot().userManager.registerNewUser(username, password, email, address, date);
//            return ResponseEntity.status(HttpStatus.OK).build();
//        }
//        catch (DuplicateUsernameException e) {
//            return ResponseEntity.status(410).body("Duplicate username");
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//            return ResponseEntity.status(408).body("Database Error");
//        } catch (DuplicateEmailException e) {
//            return ResponseEntity.status(411).body("Duplicate email");
//        }

