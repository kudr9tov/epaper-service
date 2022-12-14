package com.github.kudr9tov.epaper.services.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.FIELD)
public class OSInfoModel {
    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "version")
    private String version;
}
