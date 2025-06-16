package ar.uba.fi.ingsoft1.authorization;

import io.jsonwebtoken.Claims;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final JwtHandler jwtHandler;
    private final List<String> privilegesRank = Arrays.asList("user", "admin");
    @Autowired
    public AuthorizationService(JwtHandler jwtHandler) {
        this.jwtHandler = jwtHandler;
    }

    // Validate and extract Claims from the token
    public Claims validateTokenAndExtractClaims(String authHeader) {
        if (authHeader == null || authHeader.isBlank()) {
            throw new IllegalArgumentException("Missing authorization header");
        }

        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        return jwtHandler.decodeToken(token);
    }

    // Check if the user has the required privilege
    public boolean hasPrivilege(Claims claims, String requiredPrivilege) {
        String privilege = claims.get("privilege", String.class);

        int userPrivilegeLevel = privilegesRank.indexOf(privilege);
        int requiredPrivilegeLevel = privilegesRank.indexOf(requiredPrivilege);

        // User has access if their privilege level is greater than or equal to the required level
        return userPrivilegeLevel >= requiredPrivilegeLevel;
    }

    public Long getUserId(Claims claims) {
        return claims.get("userId", Long.class);
    }
}
