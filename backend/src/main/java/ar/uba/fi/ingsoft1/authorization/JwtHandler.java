package ar.uba.fi.ingsoft1.authorization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtException;


import java.util.Date;

@Component
public class JwtHandler {

    @Value("${jwt.secret}")
    String secretKey;

    private static final int ONE_DAY_IN_MS = 24 * 60 * 60 * 1000;

    public String generateToken(String username, Long userId, String privilege) {
        return Jwts.builder()
                .setSubject(username)
                .claim("privilege", privilege)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ONE_DAY_IN_MS))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Claims decodeToken(String token) {
        try {
            return Jwts.parser()
                       .setSigningKey(secretKey)
                       .build()  
                       .parseClaimsJws(token) 
                       .getBody();
        } catch (JwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
            throw(e); 
        }
    }
    
}
