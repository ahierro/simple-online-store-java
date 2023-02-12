package com.iron.tec.labs.ecommercejava.dto;

import java.util.List;

public class ProductPage extends PageResponseDTO<ProductDTO>{
    public ProductPage(List<ProductDTO> content) {
        super(content);
    }
}
