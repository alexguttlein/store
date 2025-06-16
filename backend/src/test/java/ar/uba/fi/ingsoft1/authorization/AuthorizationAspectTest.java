package ar.uba.fi.ingsoft1.authorization;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorizationAspectTest {

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @InjectMocks
    private AuthorizationAspect authorizationAspect;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMissingAuthorizationHeader() throws Throwable {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        ResponseEntity<?> response = (ResponseEntity<?>) authorizationAspect.checkPrivileges(joinPoint);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Missing authorization header", ((Map<?, ?>) response.getBody()).get("message"));
    }

    @Test
    void testInvalidToken() throws Throwable {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(authorizationService.validateTokenAndExtractClaims(anyString())).thenThrow(new IllegalArgumentException("Invalid token"));

        // Act
        ResponseEntity<?> response = (ResponseEntity<?>) authorizationAspect.checkPrivileges(joinPoint);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid or expired token", ((Map<?, ?>) response.getBody()).get("message"));
    }

    @Test
    void testInsufficientPrivileges() throws Throwable {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        Claims claims = mock(Claims.class);
        when(authorizationService.validateTokenAndExtractClaims(anyString())).thenReturn(claims);
        when(claims.get("privilege", String.class)).thenReturn("user");

        MethodSignature methodSignature = mock(MethodSignature.class);
        when(joinPoint.getSignature()).thenReturn(methodSignature);

        Method method = AuthorizationAspectTest.class.getMethod("mockAdminMethod");
        when(methodSignature.getMethod()).thenReturn(method);

        RequiresPrivilege annotation = method.getAnnotation(RequiresPrivilege.class);
        assertNotNull(annotation);
        when(authorizationService.hasPrivilege(claims, "admin")).thenReturn(false);

        // Act
        ResponseEntity<?> response = (ResponseEntity<?>) authorizationAspect.checkPrivileges(joinPoint);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Insufficient privileges to access this resource", ((Map<?, ?>) response.getBody()).get("message"));
    }

    @Test
    void testSuccessfulAuthorization() throws Throwable {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        Claims claims = mock(Claims.class);
        when(authorizationService.validateTokenAndExtractClaims(anyString())).thenReturn(claims);
        when(authorizationService.hasPrivilege(claims, "admin")).thenReturn(true);

        MethodSignature methodSignature = mock(MethodSignature.class);
        when(joinPoint.getSignature()).thenReturn(methodSignature);

        Method method = AuthorizationAspectTest.class.getMethod("mockAdminMethod");
        when(methodSignature.getMethod()).thenReturn(method);

        when(joinPoint.proceed()).thenReturn("Success");

        // Act
        Object result = authorizationAspect.checkPrivileges(joinPoint);

        // Assert
        assertEquals("Success", result);
        verify(joinPoint, times(1)).proceed();
    }

    @RequiresPrivilege("admin")
    public void mockAdminMethod() {
        // Mock method for testing annotation
    }
}
