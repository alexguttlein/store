package ar.uba.fi.ingsoft1.authorization;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

class JwtHandlerTest {

    private JwtHandler jwtHandler;

    @BeforeEach
    void setUp() {
        jwtHandler = new JwtHandler();
        jwtHandler.secretKey = "Tpasdfg11Tpasdfg11Tpasdfg11Tpasdfg11Tpasdfg11Tpasdfg11Tpasdfg11Tpasdfg11Tpasdfg11";  // Mocking secret key for testing
    }

    @Test
    void testGenerateToken() {
        // Given some example inputs
        String username = "johnDoe";
        Long userId = 123L;
        String privilege = "admin";

        // When we generate a token
        String token = jwtHandler.generateToken(username, userId, privilege);

        // Then the token should not be null or empty
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testDecodeToken() {
        String username = "johnDoe";
        Long userId = 123L;
        String privilege = "admin";

        String token = jwtHandler.generateToken(username, userId, privilege);

        Claims claims = jwtHandler.decodeToken(token);

        assertEquals(username, claims.getSubject());
        assertEquals(privilege, claims.get("privilege"));
        assertEquals(123, claims.get("userId"));
    }

    @Test
    void testDecodeInvalidToken() {
        String invalidToken = "invalid.token";

        JwtException thrownException = assertThrows(JwtException.class, () -> {
            jwtHandler.decodeToken(invalidToken);
        });

        assertNotNull(thrownException);
    }
}
