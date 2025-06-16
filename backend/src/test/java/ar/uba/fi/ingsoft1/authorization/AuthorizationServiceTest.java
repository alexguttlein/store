package ar.uba.fi.ingsoft1.authorization;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorizationServiceTest {

    @InjectMocks
    private AuthorizationService authorizationService;

    @Mock
    private JwtHandler jwtHandler;

    @Mock
    private Claims claims;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for validateTokenAndExtractClaims
    @Test
    void validateTokenAndExtractClaims_validToken_returnsClaims() {
        String authHeader = "Bearer validToken";
        Claims mockClaims = mock(Claims.class);
        when(jwtHandler.decodeToken("validToken")).thenReturn(mockClaims);

        Claims result = authorizationService.validateTokenAndExtractClaims(authHeader);

        assertNotNull(result);
        assertEquals(mockClaims, result);
        verify(jwtHandler).decodeToken("validToken");
    }

    @Test
    void validateTokenAndExtractClaims_missingAuthHeader_throwsException() {
        String authHeader = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authorizationService.validateTokenAndExtractClaims(authHeader));
        assertEquals("Missing authorization header", exception.getMessage());
    }

    @Test
    void validateTokenAndExtractClaims_blankAuthHeader_throwsException() {
        String authHeader = " ";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authorizationService.validateTokenAndExtractClaims(authHeader));
        assertEquals("Missing authorization header", exception.getMessage());
    }

    @Test
    void validateTokenAndExtractClaims_invalidToken_throwsException() {
        String authHeader = "Bearer invalidToken";
        when(jwtHandler.decodeToken("invalidToken")).thenThrow(new JwtException("Invalid token"));

        JwtException exception = assertThrows(JwtException.class,
                () -> authorizationService.validateTokenAndExtractClaims(authHeader));
        assertEquals("Invalid token", exception.getMessage());
    }

    @Test
    void hasPrivilege_userHasRequiredPrivilege_returnsTrue() {
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("privilege", String.class)).thenReturn("admin");

        boolean result = authorizationService.hasPrivilege(mockClaims, "user");

        assertTrue(result);
    }

    @Test
    void hasPrivilege_userLacksRequiredPrivilege_returnsFalse() {
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("privilege", String.class)).thenReturn("user");

        boolean result = authorizationService.hasPrivilege(mockClaims, "admin");

        assertFalse(result);
    }

    @Test
    void hasPrivilege_userHasSamePrivilege_returnsTrue() {
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("privilege", String.class)).thenReturn("admin");

        boolean result = authorizationService.hasPrivilege(mockClaims, "admin");

        assertTrue(result);
    }

    @Test
    void hasPrivilege_userHasHigherPrivilege_returnsTrue() {
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("privilege", String.class)).thenReturn("admin");

        boolean result = authorizationService.hasPrivilege(mockClaims, "user");

        assertTrue(result);
    }

    // Test for getUserId
    @Test
    void getUserId_validClaims_returnsUserId() {
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("userId", Long.class)).thenReturn(123L);

        Long userId = authorizationService.getUserId(mockClaims);

        assertEquals(123, userId);
    }

    @Test
    void getUserId_missingUserId_returnsNull() {
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("userID", String.class)).thenReturn(null);

        Long userId = authorizationService.getUserId(mockClaims);

        assertNull(userId);
    }
}
