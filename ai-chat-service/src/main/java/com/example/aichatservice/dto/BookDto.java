package com.example.aichatservice.dto;

import lombok.Data;

/**
 * A könyvek adatátvivő objektuma (DTO), amely tartalmazza a könyv alapvető adatait.
 */
@Data
public class BookDto {

    private Integer id;

    private String title;

    private String author;

    private int publishYear;

    private int price;

    private CategoryDto category;

    private String imgUrl;

    private String description;
}
