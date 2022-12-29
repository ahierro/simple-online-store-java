package com.iron.tec.labs.ecommercejava.config;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;

public class CustomBodyExchange extends ServerWebExchangeDecorator {
    private RequestBodyLogger requestBodyLogger;
    private ResponseBodyLogger responseBodyLogger;

    public CustomBodyExchange(ServerWebExchange exchange) {
        super(exchange);
        this.requestBodyLogger = new RequestBodyLogger(exchange.getRequest());
        this.responseBodyLogger = new ResponseBodyLogger(exchange.getResponse());
    }

    @Override
    public RequestBodyLogger getRequest() {
        return requestBodyLogger;
    }

    @Override
    public ResponseBodyLogger getResponse() {
        return responseBodyLogger;
    }
}
