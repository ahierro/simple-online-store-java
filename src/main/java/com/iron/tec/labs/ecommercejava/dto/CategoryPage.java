package com.iron.tec.labs.ecommercejava.dto;

import java.util.List;

public class CategoryPage extends PageResponseDTO<CategoryDTO>{
    public CategoryPage(List<CategoryDTO> content) {
        super(content);
    }
}
