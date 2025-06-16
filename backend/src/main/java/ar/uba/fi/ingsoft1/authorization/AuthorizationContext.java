package ar.uba.fi.ingsoft1.authorization;
import io.jsonwebtoken.Claims;

public class AuthorizationContext {
    private static final ThreadLocal<Claims> claimsThreadLocal = new ThreadLocal<>();

    public static void setClaims(Claims claims) {
        claimsThreadLocal.set(claims);
    }

    public static Claims getClaims() {
        return claimsThreadLocal.get();
    }

    public static void clear() {
        claimsThreadLocal.remove();
    }
}