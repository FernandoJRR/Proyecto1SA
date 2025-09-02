package com.sa.employee_service.auth.login.jwt.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import com.sa.employee_service.auth.application.usecases.GenerateJWTUseCase;
import com.sa.employee_service.users.domain.User;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JwtGeneratorUtilTest {

    private final GenerateJWTUseCase jwtGeneratorUtil = new GenerateJWTUseCase();

    @Test
    public void generateTokenShouldContainCorrectClaims() {
        // Arrange
        User user = mock(User.class);
        when(user.getUsername()).thenReturn("admin");

        List<String> permissions = List.of("READ", "WRITE");

        // Act
        String token = jwtGeneratorUtil.generateToken(user, permissions);

        // Assert
        assertNotNull(token);

        // Decodificar el token
        Claims claims = Jwts.parser()
                .setSigningKey(GenerateJWTUseCase.SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("admin", claims.getSubject());
        assertEquals("ROLE_USER", claims.get("rol"));
        assertTrue(claims.get("authorities") instanceof List<?>);

        @SuppressWarnings("unchecked")
        List<String> extractedPermissions = (List<String>) claims.get("authorities");
        assertTrue(extractedPermissions.contains("READ"));
        assertTrue(extractedPermissions.contains("WRITE"));

        Date now = new Date();
        assertTrue(claims.getExpiration().after(now));
        assertTrue(claims.getIssuedAt().before(claims.getExpiration()));
    }
}
