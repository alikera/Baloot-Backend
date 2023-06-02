package org.Baloot.Application;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class CorsFilter extends OncePerRequestFilter {
    @Override
    public void destroy() {}

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Expose-Headers", "*");

        if (request.getMethod().equals("OPTIONS")) {
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setHeader("Access-Control-Max-Age", "1800");
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
        } else
            filterChain.doFilter(request, response);
    }
}