package com.iron.tec.labs.ecommercejava.config;

import com.iron.tec.labs.ecommercejava.config.security.HttpConfigSetter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@SpringBootApplication
public class ControllersConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return HttpConfigSetter.setHttpConfig(http)
                .build();
    }
}
