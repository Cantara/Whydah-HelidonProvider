package net.whydah.sso.helidon;

import java.lang.annotation.*;

/**
 * Repeatable annotation for {@link Authentication}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Inherited
public @interface Authentications {
    /**
     * Repeating annotation.
     *
     * @return annotations
     */
    Authentication[] value();
}
