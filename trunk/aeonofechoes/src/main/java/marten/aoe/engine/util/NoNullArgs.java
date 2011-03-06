package marten.aoe.engine.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methods and constructors marked with this annotation are decorated by aspectj
 * aspect which checks if any of parameters is null and throws exception if this
 * is true
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.CONSTRUCTOR })
public @interface NoNullArgs {
}
