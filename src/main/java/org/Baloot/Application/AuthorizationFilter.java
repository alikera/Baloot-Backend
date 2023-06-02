package org.Baloot.Application;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.xml.bind.DatatypeConverter;
import org.Baloot.Database.Database;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;

@Component
@Order(2)
public class AuthorizationFilter extends OncePerRequestFilter {
    public static Claims parseJWT(String jwtString) {
        Key key = getKey();

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwtString)
                .getBody();
    }
    private static Key getKey() {
        String SECRET_KEY = "baloot2023";

        byte[] keyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY+SECRET_KEY+SECRET_KEY+SECRET_KEY+SECRET_KEY);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {
        try {
            ArrayList<String> urlsWithoutTokens = new ArrayList<>();
            urlsWithoutTokens.add("http://localhost:8080/api/auth/login");
            urlsWithoutTokens.add("http://localhost:8080/api/auth/register");
            urlsWithoutTokens.add("http://localhost:8080/api/auth/callback");
            if (!urlsWithoutTokens.contains(request.getRequestURL().toString())) {
                String jwtString = request.getHeader("Authorization");
                Claims claims = parseJWT(jwtString);
                request.setAttribute("user", Database.findByUsername(claims.get("username").toString()));
            }

            filterChain.doFilter(request, response);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}