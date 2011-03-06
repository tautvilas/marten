package marten.aoe.engine.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.SuppressAjWarnings;
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
                throw new IllegalArgumentException(
                        "Argument can not be null at "
                                + thisJoinPoint.getSignature()
                                + " for argument number " + (i + 1));
            }
        }
    }

    before(): methodExpectingNonNull() {
        Signature signature = thisJoinPoint.getSignature();
        if (signature instanceof MethodSignature) {
            Object args[] = thisJoinPoint.getArgs();
            Method method = ((MethodSignature)signature).getMethod();
            Annotation[][] annotations = method.getParameterAnnotations();
            for (int i = 0; i < args.length; i++) {
                for (Annotation annotation : annotations[i]) {
                    if (annotation instanceof NotNull && args[i] == null) {
                        Class<?>[] types = method.getParameterTypes();
                        throw new IllegalArgumentException("Parameter number "
                                + (i + 1) + " (" + types[i]
                                + ") can not be null for\n"
                                + thisJoinPoint.getSignature());
                    }
                }
            }
        } else {
            throw new RuntimeException(
                    "@NotNull annotation can only be applied to method parameters");
        }
    }
}