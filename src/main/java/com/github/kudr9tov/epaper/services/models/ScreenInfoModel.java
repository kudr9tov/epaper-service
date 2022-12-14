package com.github.kudr9tov.epaper.services.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.FIELD)
public class ScreenInfoModel {
    @XmlAttribute(name = "width", required = true)
    private short width;
    @XmlAttribute(name = "height", required = true)
    private short height;
    @XmlAttribute(name = "dpi", required = true)
    private short dpi;
}
