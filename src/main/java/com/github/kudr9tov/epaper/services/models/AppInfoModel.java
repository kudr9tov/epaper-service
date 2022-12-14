package com.github.kudr9tov.epaper.services.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.FIELD)
public class AppInfoModel {
    @XmlElement(name = "newspaperName", required = true)
    private String newspaperName;
    @XmlElement(name = "version")
    private String version;
}
