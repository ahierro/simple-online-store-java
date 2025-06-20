package com.iron.tec.labs.ecommercejava.config.logging;

import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;

public class CustomBodyExchange extends ServerWebExchangeDecorator {
    private final RequestBodyLogger requestBodyLogger;
    private final ResponseBodyLogger responseBodyLogger;

    public CustomBodyExchange(ServerWebExchange exchange) {
        super(exchange);
        this.requestBodyLogger = new RequestBodyLogger(exchange.getRequest());
        this.responseBodyLogger = new ResponseBodyLogger(exchange.getResponse());
    }

    @Override
    public @NonNull RequestBodyLogger getRequest() {
        return requestBodyLogger;
    }

    @Override
    public @NonNull ResponseBodyLogger getResponse() {
        return responseBodyLogger;
    }
}
