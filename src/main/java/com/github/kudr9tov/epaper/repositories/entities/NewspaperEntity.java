package com.github.kudr9tov.epaper.repositories.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "news")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewspaperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String newspaperName;
    @Embedded
    private ScreenInfoEmbedded screenInfo;
    @Embedded
    private FileModelEmbedded fileModel;
}
