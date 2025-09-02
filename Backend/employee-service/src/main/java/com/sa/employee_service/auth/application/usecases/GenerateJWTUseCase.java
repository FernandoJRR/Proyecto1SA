package com.sa.employee_service.auth.application.usecases;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.sa.employee_service.auth.application.outputports.GenerateJWTTokenOutputPort;
import com.sa.employee_service.users.domain.User;
import com.sa.employee_service.users.infrastructure.repositoryadapter.models.UserEntity;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class GenerateJWTUseCase implements GenerateJWTTokenOutputPort {

    public static final long JWT_TOKEN_TIME_VALIDITY = 86400000; // 1 día en milisegundos
    public static final String SECRET_KEY = "p9Q4f2Z7xV1mA0sB8eR3tY6uI5oL2kJ9 ";

    @Override
    public String generateToken(User user, List<String> permissions) {
        // Extraer roles y permisos
        Map<String, Object> claims = new HashMap<>();

        // Agregar claims personalizados
        claims.put("rol", "ROLE_USER");
        claims.put("authorities", permissions);

        // Generar el token
        return createToken(claims, user.getUsername());
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_TIME_VALIDITY))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

}
