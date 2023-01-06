package com.iron.tec.labs.ecommercejava.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.List;
import java.util.Map;

@Slf4j
@WritingConverter
@AllArgsConstructor
public class ListOfStringToJsonConverter implements Converter<List<String>, Json> {

    private final ObjectMapper objectMapper;

    @Override
    public Json convert(List<String> source) {
        try {
            return Json.of(objectMapper.writeValueAsString(source));
        } catch (JsonProcessingException e) {
            log.error("Error occurred while serializing map to JSON: {}", source, e);
        }
        return Json.of("");
    }

}
