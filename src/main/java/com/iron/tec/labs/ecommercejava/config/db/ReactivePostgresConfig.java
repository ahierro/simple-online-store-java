package com.iron.tec.labs.ecommercejava.config.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iron.tec.labs.ecommercejava.mappers.converters.JsonToListOfStringConverter;
import com.iron.tec.labs.ecommercejava.mappers.converters.JsonToMapConverter;
import com.iron.tec.labs.ecommercejava.mappers.converters.ListOfStringToJsonConverter;
import com.iron.tec.labs.ecommercejava.mappers.converters.MapToJsonConverter;
import io.r2dbc.spi.ConnectionFactory;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
@AllArgsConstructor
public class ReactivePostgresConfig extends AbstractR2dbcConfiguration {

    private final ObjectMapper objectMapper;

//    private final DatasourceProps datasourceProps;
//
//    /**
//     * @noinspection NullableProblems
//     */
//    @Override
//    public ConnectionFactory connectionFactory() {
//        return ConnectionFactories.get(datasourceProps.getPrefix() + datasourceProps.getUsername() + ":" +
//                datasourceProps.getPassword() + "@" + datasourceProps.getHost() + ":" + datasourceProps.getPort() +
//                "/" + datasourceProps.getDatabase());
//    }

    @Override
    public ConnectionFactory connectionFactory() {
        return null;
    }

    @Bean
    @Override
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new JsonToMapConverter(objectMapper));
        converters.add(new MapToJsonConverter(objectMapper));
        converters.add(new JsonToListOfStringConverter(objectMapper));
        converters.add(new ListOfStringToJsonConverter(objectMapper));
        return new R2dbcCustomConversions(getStoreConversions(), converters);
    }

}

