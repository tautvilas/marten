package marten.aoe.aspectj;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;

@SuppressAjWarnings("adviceDidNotMatch")
public aspect RestrictionEnforcer {

    private class AnnotatedParameters {
        private Annotation[][] annotations;
        private Object[] arguments;
        public AnnotatedParameters(Annotation[][] annotations, Object[] arguments) {
            this.annotations = annotations;
            this.arguments = arguments;
            if (this.annotations.length != this.arguments.length) {
                throw new RuntimeException("Annotations mismatch the number of arguments "+this.annotations.length+" against "+this.arguments.length);
            }
        }
        public int numOfArguments() {
            return this.arguments.length;
        }
        public Annotation[] getAnnotations(int index) {
            return this.annotations[index];
        }
        public Object getArgument(int index) {
            return this.arguments[index];
        }
    }

    private AnnotatedParameters extract(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature
                || signature instanceof ConstructorSignature) {
            Annotation[][] annotations = null;
            Object[] args = null;
            if (signature instanceof MethodSignature) {
                Method method = ((MethodSignature)signature).getMethod();
                annotations = method.getParameterAnnotations();
                args = joinPoint.getArgs();
            } else {
                Constructor<?> constructor = ((ConstructorSignature)signature)
                .getConstructor();
                annotations = constructor.getParameterAnnotations();
                args = joinPoint.getArgs();
            }
            Object[] finalArgs = new Object[annotations.length];
            for (int i = 0; i < annotations.length; i++) {
                finalArgs[i] = args[i + args.length - annotations.length];
            }
            return this.new AnnotatedParameters(annotations, finalArgs);
        }
        return null;   
    }

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
                        + (i + 1) + " can not be null for\n"
                        + thisJoinPoint.getSignature());
            }
        }
    }

    before(): methodExpectingNonNull() {
        AnnotatedParameters params = this.extract(thisJoinPoint);
        if (params == null) {
            throw new RuntimeException(
            "@NotNull annotation can only be applied for method and constructor parameters");
        }
        for (int i = 0; i < params.numOfArguments(); i++) {
            for (Annotation annotation : params.getAnnotations(i)) {
                if (annotation instanceof NotNull && params.getArgument(i) == null) {
                    throw new IllegalArgumentException("Parameter number "
                            + (i + 1) + " can not be null for\n"
                            + thisJoinPoint.getSignature());                    
                }
            }
        }
    }

    before(): methodWithNoNullCollection() {
        AnnotatedParameters params = this.extract(thisJoinPoint);
        if (params == null) {
            throw new RuntimeException(
            "@NoNullEntries annotation can only be applied for method and constructor parameters");
        }
        for (int i = 0; i < params.numOfArguments(); i++) {
            for (Annotation annotation : params.getAnnotations(i)) {
                if (annotation instanceof NoNullEntries) {
                    Object argument = params.getArgument(i);
                    if (argument != null) {
                        if (argument instanceof Object[]) {
                            for (Object object : (Object[])argument) {
                                if (object == null) {
                                    throw new IllegalArgumentException("Parameter number "
                                            + (i + 1) + " can not contain null values for\n"
                                            + thisJoinPoint.getSignature());
                                }                                    
                            }
                        }
                        else if (argument instanceof Collection<?>) {
                            for (Object object : (Collection<?>)argument) {
                                if (object == null) {
                                    throw new IllegalArgumentException("Parameter number "
                                            + (i + 1) + " can not contain null values for\n"
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
                                + (i + 1) + " can neither contain null values nor be null itself for\n"
                                + thisJoinPoint.getSignature());
                    }
                }                        
            }

        }
    }

    before(): methodWithRangedIntegers() {
        AnnotatedParameters params = this.extract(thisJoinPoint);
        if (params == null) {
            throw new RuntimeException(
            "@NoNullEntries annotation can only be applied for method and constructor parameters");
        }
        for (int i = 0; i < params.numOfArguments(); i++) {
            for (Annotation annotation : params.getAnnotations(i)) {
                if (annotation instanceof Range) {
                    Object argument = params.getArgument(i);
                    if (argument != null && argument instanceof Number) {
                        Number number = (Number)argument;
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
}