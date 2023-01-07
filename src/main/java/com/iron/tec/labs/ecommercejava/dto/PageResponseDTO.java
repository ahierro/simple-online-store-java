package com.iron.tec.labs.ecommercejava.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@JsonIgnoreProperties({"pageable","last","size","sort","first","numberOfElements","empty"})
public class PageResponseDTO<T> extends PageImpl<T> {
    public PageResponseDTO(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PageResponseDTO(List<T> content) {
        super(content);
    }
}
