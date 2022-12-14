package com.github.kudr9tov.epaper.services.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceInfoModel {
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "name")
    private String name;
    @XmlElement(name = "screenInfo", required = true)
    private ScreenInfoModel screenInfo;
    @XmlElement(name = "osInfo")
    private OSInfoModel osInfo;
    @XmlElement(name = "appInfo", required = true)
    private AppInfoModel appInfo;
}
