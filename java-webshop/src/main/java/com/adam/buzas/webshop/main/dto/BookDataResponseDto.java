package com.adam.buzas.webshop.main.dto;

import lombok.Data;

@Data
public class BookDataResponseDto {
    private String title;
    private String author;
    private String description;
    private String error;
}
