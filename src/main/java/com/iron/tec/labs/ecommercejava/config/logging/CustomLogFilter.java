package com.iron.tec.labs.ecommercejava.config.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
//@Component
public class CustomLogFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        CustomBodyExchange bodyExchange = new CustomBodyExchange(serverWebExchange);
        return webFilterChain.filter(bodyExchange)
                .doOnSuccess(se -> {
            log.info("{} Body request {}", serverWebExchange.getLogPrefix(), bodyExchange.getRequest().getFullBody());
            log.info("{} Body response {} ", serverWebExchange.getLogPrefix(), bodyExchange.getResponse().getFullBody());
        });
    }
}
