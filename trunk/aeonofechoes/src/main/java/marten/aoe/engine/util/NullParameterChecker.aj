package marten.aoe.engine.util;

public aspect NullParameterChecker {
    pointcut methodExpectingNonNull():
        execution(@NoNullArgs * *(..));

    before(): methodExpectingNonNull() {
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
}