package com.iron.tec.labs.ecommercejava.config.logging;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;

public class RequestBodyLogger extends ServerHttpRequestDecorator {
    private final StringBuilder body = new StringBuilder();

    public RequestBodyLogger(ServerHttpRequest delegate) {
        super(delegate);
    }

    @Override
    public Flux<DataBuffer> getBody() {
        return super.getBody().doOnNext(this::capture);
    }

    private void capture(DataBuffer buffer) {
        this.body.append(StandardCharsets.UTF_8.decode(buffer.toByteBuffer()));
    }

    public String getFullBody() {
        return this.body.toString();
    }
}
