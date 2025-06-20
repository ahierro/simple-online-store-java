package com.iron.tec.labs.ecommercejava.config.logging;

import java.nio.charset.StandardCharsets;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.lang.NonNull;

import reactor.core.publisher.Flux;

public class RequestBodyLogger extends ServerHttpRequestDecorator {
    private final StringBuilder body = new StringBuilder();

    public RequestBodyLogger(ServerHttpRequest delegate) {
        super(delegate);
    }

    @Override
    public @NonNull Flux<DataBuffer> getBody() {
        return super.getBody().doOnNext(this::capture);
    }

    private void capture(DataBuffer buffer) {
        try (var it = buffer.readableByteBuffers()) {
            it.forEachRemaining(bb -> this.body.append(StandardCharsets.UTF_8.decode(bb)));
        }
    }

    public String getFullBody() {
        return this.body.toString();
    }
}
