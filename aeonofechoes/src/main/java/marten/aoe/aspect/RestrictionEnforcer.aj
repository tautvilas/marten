package marten.aoe.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;

import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;

@SuppressAjWarnings("adviceDidNotMatch")
public aspect RestrictionEnforcer {
    pointcut methodExpectingNonNull():
        execution(* *(.., @NotNull (*), ..)) || 
        execution(*.new(.., @NotNull (*), ..));

    pointcut methodExpectingNoNulls():
        execution(@NoNullArgs * *(..)) ||
        execution(@NoNullArgs *.new(..));
    
    pointcut methodWithNoNullCollection():
        execution(* *(.., @NoNullEntries (*), ..)) ||
        execution(*.new(.., @NoNullEntries (*), ..));
    
    pointcut methodWithRangedIntegers():
        execution(* *(.., @Range (*), ..)) ||
        execution(*.new(.., @Range (*), ..));

    before(): methodExpectingNoNulls() {
        Object o[] = thisJoinPoint.getArgs();
        for (int i = 0; i < o.length; i++) {
            if (o[i] == null) {
                throw new IllegalArgumentException("Parameter number "
                        + i + " can not be null for\n"
                        + thisJoinPoint.getSignature());
            }
        }
    }

    before(): methodExpectingNonNull() {
        Signature signature = thisJoinPoint.getSignature();
        if (signature instanceof MethodSignature
                || signature instanceof ConstructorSignature) {
            Annotation[][] annotations = null;
            Object[] args = null;
            if (signature instanceof MethodSignature) {
                Method method = ((MethodSignature)signature).getMethod();
                annotations = method.getParameterAnnotations();
                args = thisJoinPoint.getArgs();
            } else {
                Constructor<?> constructor = ((ConstructorSignature)signature)
                        .getConstructor();
                annotations = constructor.getParameterAnnotations();
                Object[] tempArgs = thisJoinPoint.getArgs();
                args = new Object[tempArgs.length - 1];
                for (int index = 1; index < tempArgs.length; index++) {
                    args[index - 1] = tempArgs[index];
                }
            }
            for (int i = 0; i < args.length; i++) {
                for (Annotation annotation : annotations[i]) {
                    if (annotation instanceof NotNull && args[i] == null) {
                        throw new IllegalArgumentException("Parameter number "
                                + i + " can not be null for\n"
                                + thisJoinPoint.getSignature());
                    }
                }
            }
        } else {
            throw new RuntimeException(
                    "@NotNull annotation can only be applied for method and constructor parameters");
        }
    }
    
    before(): methodWithNoNullCollection() {
        Signature signature = thisJoinPoint.getSignature();
        if (signature instanceof MethodSignature
                || signature instanceof ConstructorSignature) {
            Annotation[][] annotations = null;
            Object[] args = null;
            if (signature instanceof MethodSignature) {
                Method method = ((MethodSignature)signature).getMethod();
                annotations = method.getParameterAnnotations();
                args = thisJoinPoint.getArgs();
            } else {
                Constructor<?> constructor = ((ConstructorSignature)signature)
                        .getConstructor();
                annotations = constructor.getParameterAnnotations();
                Object[] tempArgs = thisJoinPoint.getArgs();
                args = new Object[tempArgs.length - 1];
                for (int index = 1; index < tempArgs.length; index++) {
                    args[index - 1] = tempArgs[index];
                }
            }
            for (int i = 0; i < args.length; i++) {
                for (Annotation annotation : annotations[i]) {
                    if (annotation instanceof NoNullEntries) {
                        if (args[i] != null) {
                            if (args[i] instanceof Object[]) {
                                for (Object object : (Object[])args[i]) {
                                    if (object == null) {
                                        throw new IllegalArgumentException("Parameter number "
                                                + i + " can not contain null values for\n"
                                                + thisJoinPoint.getSignature());
                                    }                                    
                                }
                            }
                            else if (args[i] instanceof Collection<?>) {
                                for (Object object : (Collection<?>)args[i]) {
                                    if (object == null) {
                                        throw new IllegalArgumentException("Parameter number "
                                                + i + " can not contain null values for\n"
                                                + thisJoinPoint.getSignature());
                                    }
                                }
                            }
                            else {
                                throw new RuntimeException("@NoNullEntries annotation can only be applied for "+
                                "method or constructor parameters which are arrays or collections");
                            }
                        }
                        else {
                            throw new IllegalArgumentException("Parameter number "
                                    + i + " can neither contain null values nor be null itself for\n"
                                    + thisJoinPoint.getSignature());
                        }
                    }                        
                }
            }
        } else {
            throw new RuntimeException(
                    "@NoNullEntries annotation can only be applied for method and constructor parameters");
        }
    }
    
    before(): methodWithRangedIntegers() {
        Signature signature = thisJoinPoint.getSignature();
        if (signature instanceof MethodSignature
                || signature instanceof ConstructorSignature) {
            Annotation[][] annotations = null;
            Object[] args = null;
            if (signature instanceof MethodSignature) {
                Method method = ((MethodSignature)signature).getMethod();
                annotations = method.getParameterAnnotations();
                args = thisJoinPoint.getArgs();
            } else {
                Constructor<?> constructor = ((ConstructorSignature)signature)
                        .getConstructor();
                annotations = constructor.getParameterAnnotations();
                Object[] tempArgs = thisJoinPoint.getArgs();
                args = new Object[tempArgs.length - 1];
                for (int index = 1; index < tempArgs.length; index++) {
                    args[index - 1] = tempArgs[index];
                }
            }
            for (int i = 0; i < args.length; i++) {
                for (Annotation annotation : annotations[i]) {
                    if (annotation instanceof Range) {
                        if (args[i] != null) {
                            if (args[i] instanceof Number) {
                                Number number = (Number)args[i];
                                Range range = (Range)annotation;
                                if (number.doubleValue() < range.from() || number.doubleValue() > range.to()) {
                                    throw new IllegalArgumentException("Parameter number "
                                            + (i + 1) + " must be within the range from "
                                            + range.from() + " to " 
                                            + range.to() + " inclusive for\n"
                                            + thisJoinPoint.getSignature());
                                }
                            }
                            else {
                                throw new RuntimeException("@Range annotation can only be applied for "+
                                "method or constructor parameters which are numbers");
                            }
                        }
                    }                        
                }
            }
        } else {
            throw new RuntimeException(
                    "@NoNullEntries annotation can only be applied for method and constructor parameters");
        }
    }
}