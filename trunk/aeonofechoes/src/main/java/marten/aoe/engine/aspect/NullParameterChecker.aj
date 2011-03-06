package marten.aoe.engine.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;

@SuppressAjWarnings("adviceDidNotMatch")
public aspect NullParameterChecker {
    pointcut methodExpectingNonNull():
        execution(* *(.., @NotNull (*), ..));

    pointcut methodExpectingNoNulls():
        execution(@NoNullArgs * *(..));

    before(): methodExpectingNoNulls() {
        Object o[] = thisJoinPoint.getArgs();
        for (int i = 0; i < o.length; i++) {
            if (o[i] == null) {
                throw new IllegalArgumentException("Parameter number "
                        + (i + 1) + " can not be null for\n"
                        + thisJoinPoint.getSignature());
            }
        }
    }

    before(): methodExpectingNonNull() {
        Signature signature = thisJoinPoint.getSignature();
        if (signature instanceof MethodSignature
                || signature instanceof ConstructorSignature) {
            Annotation[][] annotations = null;
            if (signature instanceof MethodSignature) {
                Method method = ((MethodSignature)signature).getMethod();
                annotations = method.getParameterAnnotations();
            } else {
                Constructor<?> constructor = ((ConstructorSignature)signature)
                        .getConstructor();
                annotations = constructor.getParameterAnnotations();
            }
            Object args[] = thisJoinPoint.getArgs();
            for (int i = 0; i < args.length; i++) {
                for (Annotation annotation : annotations[i]) {
                    if (annotation instanceof NotNull && args[i] == null) {
                        throw new IllegalArgumentException("Parameter number "
                                + (i + 1) + " can not be null for\n"
                                + thisJoinPoint.getSignature());
                    }
                }
            }
        } else {
            throw new RuntimeException(
                    "@NotNull annotation can only be applied for method and constructor parameters");
        }
    }
}