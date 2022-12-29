package com.iron.tec.labs.ecommercejava.config;

import com.iron.tec.labs.ecommercejava.util.LoggingUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Configuration
@Log4j2
public class RequestIdLoggerFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        CustomBodyExchange bodyExchange = new CustomBodyExchange(exchange);
        String requestId = exchange.getLogPrefix();
        return chain
                .filter(bodyExchange)
                .doOnEach(LoggingUtils.logOnError(e -> {
                    log.info("Request Body OnError {}",bodyExchange.getRequest().getFullBody());
                    log.error(e.getMessage(), e);
                }))
                .doOnSuccess(se -> {
                    log.info("{} Body request {}", bodyExchange.getLogPrefix(), bodyExchange.getRequest().getFullBody());
                    log.info("{} Body response {} ", bodyExchange.getLogPrefix(), bodyExchange.getResponse().getFullBody());
                })
                .contextWrite(Context.of("requestId", requestId));
    }

}