package com.iron.tec.labs.ecommercejava.util;

import org.slf4j.MDC;
import reactor.core.publisher.Signal;

import java.util.Optional;
import java.util.function.Consumer;


public class LoggingUtils {

    public static final String REQUEST_ID = "requestId";

    private LoggingUtils() {
    }

    public static <T> Consumer<Signal<T>> logOnComplete(Consumer<T> logStatement) {
        return signal -> {
            if (!signal.isOnComplete()) return;
            Optional<String> apiIDMaybe = signal.getContextView().getOrEmpty(REQUEST_ID);

            apiIDMaybe.ifPresentOrElse(apiID -> {
                try (MDC.MDCCloseable closeable = MDC.putCloseable(REQUEST_ID, apiID)) {
                    logStatement.accept(signal.get());
                }
            }, () -> logStatement.accept(signal.get()));
        };
    }

    public static Consumer<Signal<?>> logOnError(Consumer<Throwable> errorLogStatement) {
        return signal -> {
            if (!signal.isOnError()) return;
            Optional<String> toPutInMdc2 = signal.getContextView().getOrEmpty(REQUEST_ID);

            toPutInMdc2.ifPresentOrElse(tpim -> {
                        try (MDC.MDCCloseable cMdc = MDC.putCloseable(REQUEST_ID, tpim)) {
                            errorLogStatement.accept(signal.getThrowable());
                        }
                    },
                    () -> errorLogStatement.accept(signal.getThrowable()));
        };
    }
}
