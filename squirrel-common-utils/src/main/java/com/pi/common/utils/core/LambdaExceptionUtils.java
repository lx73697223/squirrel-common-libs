package com.pi.common.utils.core;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 See http://stackoverflow.com/questions/18198176/java-8-lambda-function-that-throws-exception/27661562#27661562
 */
public final class LambdaExceptionUtils {

    @FunctionalInterface
    public interface Function_WithExceptions<T, R, E extends Exception> {
        R apply(T t) throws E;
    }

    @FunctionalInterface
    public interface Consumer_WithExceptions<T, E extends Exception> {
        void accept(T t) throws E;
    }

    @FunctionalInterface
    public interface Supplier_WithExceptions<T, E extends Exception> {
        T get() throws E;
    }

    /**
     .map(rethrowFunction(name -> Class.forName(name))) or .map(rethrowFunction(Class::forName))
     */
    public static <T, R, E extends Exception> Function<T, R> rethrowFunction(Function_WithExceptions<T, R, E> function)
            throws E {
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception exception) {
                throwActualException(exception);
                return null;
            }
        };
    }

    public static <T, E extends Exception> Consumer<T> rethrowConsumer(Consumer_WithExceptions<T, E> function)
            throws E {
        return t -> {
            try {
                function.accept(t);
            } catch (Exception exception) {
                throwActualException(exception);
            }
        };
    }

    public static <T, E extends Exception> Supplier<T> rethrowSupplier(Supplier_WithExceptions<T, E> function)
            throws E {
        return () -> {
            try {
                return function.get();
            } catch (Exception exception) {
                throwActualException(exception);
                return null;
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static <E extends Exception> void throwActualException(Exception exception)
            throws E {
        throw (E) exception;
    }

}
