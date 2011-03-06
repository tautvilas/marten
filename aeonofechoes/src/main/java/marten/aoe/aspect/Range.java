package marten.aoe.aspect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method or constructor parameters which are integers are
 * expected to be in the defined range (including the end points).
 * This is enforced by {@link RestrictionEnforcer}.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Range {
    double from();
    double to();
}
