package ar.uba.fi.ingsoft1.authorization;

import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class AuthorizationAspect {

    private final AuthorizationService authorizationService;
    private final HttpServletRequest request;

    @Autowired
    public AuthorizationAspect(AuthorizationService authorizationService, HttpServletRequest request) {
        this.authorizationService = authorizationService;
        this.request = request;
    }

    @Around("@annotation(RequiresPrivilege)")
    public Object checkPrivileges(ProceedingJoinPoint joinPoint) throws Throwable {
        Map<String, String> response = new HashMap<>();
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.isBlank()) {
            response.put("message", "Missing authorization header");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        try {
            Claims claims = authorizationService.validateTokenAndExtractClaims(authHeader);
            AuthorizationContext.setClaims(claims);

            // Retrieve the required privilege from the annotation
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            RequiresPrivilege annotation = signature.getMethod().getAnnotation(RequiresPrivilege.class);
            String requiredPrivilege = annotation.value();

            if (!authorizationService.hasPrivilege(claims, requiredPrivilege)) {
                response.put("message", "Insufficient privileges to access this resource");
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            response.put("message", "Invalid or expired token");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // Proceed with the original method if authorized
        try {
            return joinPoint.proceed();
        } finally {
            AuthorizationContext.clear();
        }
    }
}
