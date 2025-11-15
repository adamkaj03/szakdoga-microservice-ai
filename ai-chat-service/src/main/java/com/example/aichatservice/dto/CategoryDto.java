package com.example.aichatservice.dto;

import lombok.Data;

/**
 * A könyv kategóriájának adatátvivő objektuma (DTO), amely tartalmazza a kategória alapvető adatait.
 */
@Data
public class CategoryDto {

    private Integer id;

    private String name;
}
