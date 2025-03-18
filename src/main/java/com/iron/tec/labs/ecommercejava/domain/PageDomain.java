package com.iron.tec.labs.ecommercejava.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PageDomain<T> {
    private List<T> content;
    private Integer totalPages;
    private Integer totalElements;
    private Integer number;
}
