package com.adam.buzas.webshop.main.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "archive_log")
public class ArchiveLog {

    @Id
    private Integer id;

    private Integer archiveYear;

    private String status;

    private Integer archivedCount;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
