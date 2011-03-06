package marten.aoe.engine.aspect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method or constructor parameters marked with this annotation are decorated by
 * aspectj aspect which checks if the parameter is null and throws exception if
 * that is true
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface NotNull {

}
