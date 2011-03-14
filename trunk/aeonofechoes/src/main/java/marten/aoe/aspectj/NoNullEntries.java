package marten.aoe.aspectj;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Method or constructor parameters that are arrays or collections,
 * annotated with this, are expected to have no null values in them.
 * This is enforced by {@link RestrictionEnforcer}*/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface NoNullEntries {}
