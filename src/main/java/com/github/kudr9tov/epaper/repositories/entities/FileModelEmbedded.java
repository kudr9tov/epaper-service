package com.github.kudr9tov.epaper.repositories.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.kudr9tov.epaper.utils.LocalDateTimeSerializer;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileModelEmbedded {
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime upload;
    private String originalFilename;
    private String fileLink;
}
