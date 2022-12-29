package com.iron.tec.labs.ecommercejava.util;

import org.slf4j.MDC;
import reactor.core.publisher.Signal;

import java.util.Optional;
import java.util.function.Consumer;

public class LoggingUtils {
    public static <T> Consumer<Signal<T>> logOnComplete(Consumer<T> logStatement) {
        return signal -> {
            if (!signal.isOnComplete()) return;
            Optional<String> apiIDMaybe = signal.getContextView().getOrEmpty("requestId");

            apiIDMaybe.ifPresentOrElse(apiID -> {
                try (MDC.MDCCloseable closeable = MDC.putCloseable("requestId", apiID)) {
                    logStatement.accept(signal.get());
                }
            }, () -> logStatement.accept(signal.get()));
        };
    }

    public static Consumer<Signal<?>> logOnError(Consumer<Throwable> errorLogStatement) {
        return signal -> {
            if (!signal.isOnError()) return;
            Optional<String> toPutInMdc2 = signal.getContextView().getOrEmpty("requestId");

            toPutInMdc2.ifPresentOrElse(tpim -> {
                        try (MDC.MDCCloseable cMdc = MDC.putCloseable("requestId", tpim)) {
                            errorLogStatement.accept(signal.getThrowable());
                        }
                    },
                    () -> errorLogStatement.accept(signal.getThrowable()));
        };
    }
}
