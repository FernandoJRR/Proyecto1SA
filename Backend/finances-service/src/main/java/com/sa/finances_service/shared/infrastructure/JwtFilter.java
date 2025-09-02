package com.sa.finances_service.shared.infrastructure;

import java.io.IOException;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtFilter implements Filter {
   private static final ThreadLocal<String> currentToken = new ThreadLocal<>();
    public static final String SECRET_KEY = "p9Q4f2Z7xV1mA0sB8eR3tY6uI5oL2kJ9 ";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String authHeader = ((HttpServletRequest) req).getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            validateToken(jwt);
            currentToken.set(jwt);
        }
        try {
            chain.doFilter(req, res);
        } finally {
            currentToken.remove();
        }
    }

    public static String getCurrentToken() {
        return currentToken.get();
    }

    private Claims validateToken(String token) {
        return Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
