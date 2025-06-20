package com.iron.tec.labs.ecommercejava.config.logging;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import org.springframework.lang.NonNull;

public class ResponseBodyLogger extends ServerHttpResponseDecorator {
    private final StringBuilder body = new StringBuilder();

    public ResponseBodyLogger(ServerHttpResponse delegate) {
        super(delegate);
    }

    @Override
    public @NonNull Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
        Flux<DataBuffer> buffer = Flux.from(body);
        return super.writeWith(buffer.doOnNext(this::capture));
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
