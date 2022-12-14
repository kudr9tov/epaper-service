package com.github.kudr9tov.epaper.services.models;


import com.github.kudr9tov.epaper.utils.LocalDateXmlAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.FIELD)
public class GetPagesModel {
    @XmlAttribute(name = "editionDefId")
    private long editionDefId;
    @XmlAttribute(name = "publicationDate")
    @XmlJavaTypeAdapter(LocalDateXmlAdapter.class)
    private LocalDate publicationDate;
}
