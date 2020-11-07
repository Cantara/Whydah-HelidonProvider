package net.whydah.sso.helidon;

import io.helidon.security.SubjectType;

import java.lang.annotation.*;

/**
 * Authentication annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Inherited
@Repeatable(Authentications.class)
public @interface Authentication  {
    /**
     * Name of the principal.
     *
     * @return principal name
     */
    String value();

    /**
     * Type of the subject, defaults to user.
     *
     * @return type
     */
    SubjectType type() default SubjectType.USER;

    /**
     * Granted roles.
     *
     * @return array of roles
     */
    String[] roles() default "";

    /**
     * Granted scopes.
     *
     * @return array of scopes
     */
    String[] scopes() default "";
}
