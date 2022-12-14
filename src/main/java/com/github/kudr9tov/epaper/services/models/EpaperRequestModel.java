package com.github.kudr9tov.epaper.services.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "epaperRequest")
public class EpaperRequestModel {
    @XmlElement(name = "deviceInfo", required = true)
    private DeviceInfoModel deviceInfoModel;
    @XmlElement(name = "getPages")
    private GetPagesModel getPages;
}
