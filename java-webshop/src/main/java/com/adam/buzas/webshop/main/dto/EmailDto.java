package com.adam.buzas.webshop.main.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmailDto implements Serializable {
    private String to;
    private String subject;
    private String body;
}
