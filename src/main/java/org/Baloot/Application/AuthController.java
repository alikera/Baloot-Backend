package org.Baloot.Application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;
import org.Baloot.Baloot;
import org.Baloot.Database.Database;
import org.Baloot.Entities.User;
import org.Baloot.Exception.DuplicateEmailException;
import org.Baloot.Exception.DuplicateUsernameException;
import org.Baloot.Exception.IncorrectPasswordException;
import org.Baloot.Exception.UserNotFoundException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
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

            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");

                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(responseString);
                String accessToken = rootNode.get("access_token").asText();
                System.out.println("Access token: " + accessToken);

                HttpGet get = new HttpGet("https://api.github.com/user");
                get.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
                response = client.execute(get);
                entity = response.getEntity();
                responseString = EntityUtils.toString(entity, "UTF-8");
                System.out.println("RESPONSEE:");
                System.out.println(responseString);

                mapper = new ObjectMapper();
                rootNode = mapper.readTree(responseString);
                String username = rootNode.get("login").asText();
                System.out.println("username: " + username);

                String email = rootNode.get("email").asText();
                System.out.println("email: " + email);

                String name = rootNode.get("name").asText();
                System.out.println("name: " + name);

                String address = rootNode.get("location").asText();
                System.out.println("address: " + address);

                String birth_date = rootNode.get("created_at").asText();
                birth_date = LocalDate.parse(birth_date, DateTimeFormatter.ISO_DATE_TIME).minusYears(18).format(DateTimeFormatter.ISO_DATE);
                System.out.println("birth_date: " + birth_date);

                User user = new User(username, null, null, email, birth_date, address, 0);
                Database.insertUser(user);

                HashMap<String, String> returnRes = new HashMap<>();
                returnRes.put("token", generateJwtToken(username));
                return ResponseEntity.status(200).body(returnRes);
            } else {
                System.out.println("Error: " + response.getStatusLine().getStatusCode());
                System.out.println(response.getEntity().getContent().toString());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

