package com.iron.tec.labs.ecommercejava.config.logging;

import com.iron.tec.labs.ecommercejava.util.LoggingUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@Log4j2
public class RequestIdLoggerFilter implements WebFilter {

    private static final Set<String> excludePaths = Stream.of("/signup", "/login", "/logout")
            .collect(Collectors.toSet());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        CustomBodyExchange bodyExchange = new CustomBodyExchange(exchange);
        String requestId = exchange.getLogPrefix();

        return chain
                .filter(bodyExchange)
                .doOnEach(LoggingUtils.logOnError(e -> {
                    if(!excludePaths.contains(exchange.getRequest().getPath().pathWithinApplication().value())){
                        log.info("Request Body OnError {}",bodyExchange.getRequest().getFullBody());
                    }
                    log.error(e.getMessage(), e);
                }))
                .doOnSuccess(se -> {
                    if(!excludePaths.contains(exchange.getRequest().getPath().pathWithinApplication().value())){
                        log.info("{} Body request {}", bodyExchange.getLogPrefix(), bodyExchange.getRequest().getFullBody());
                        log.info("{} Body response {} ", bodyExchange.getLogPrefix(), bodyExchange.getResponse().getFullBody());
                    }
                })
                .contextWrite(Context.of("requestId", requestId));
    }

}