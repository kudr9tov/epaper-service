package com.github.kudr9tov.epaper.repositories.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScreenInfoEmbedded {
    private short width;
    private short height;
    private short dpi;
}
