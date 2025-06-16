package ar.uba.fi.ingsoft1.authorization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPrivilege {
    String value(); // Specify the required privilege, e.g., "admin" or "user"
}