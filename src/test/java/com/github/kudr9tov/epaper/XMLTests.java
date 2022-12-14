package com.github.kudr9tov.epaper;

import com.github.kudr9tov.epaper.exceptions.IllegalXmlFileStructureException;
import com.github.kudr9tov.epaper.services.models.AppInfoModel;
import com.github.kudr9tov.epaper.services.models.DeviceInfoModel;
import com.github.kudr9tov.epaper.services.models.EpaperRequestModel;
import com.github.kudr9tov.epaper.services.models.GetPagesModel;
import com.github.kudr9tov.epaper.services.models.OSInfoModel;
import com.github.kudr9tov.epaper.services.models.ScreenInfoModel;
import com.github.kudr9tov.epaper.utils.XmlUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class XMLTests {

    @Test
    public void XSD_Valid_Test() throws Exception {
        MockMultipartFile file = createMultipartFile(ResourceUtils.getFile("classpath:sample.xml"));
        assertDoesNotThrow(() -> XmlUtils.validate(file));
    }

    @Test
    public void XSD_Validation_Exception_Test() throws Exception {
        MockMultipartFile file = createMultipartFile(ResourceUtils.getFile("classpath:wrong-sample.xml"));
        assertThrows(IllegalXmlFileStructureException.class, () -> XmlUtils.validate(file));
    }

    @Test
    public void XSDV_Unmarshall_Test() throws Exception {
        MockMultipartFile file = createMultipartFile(ResourceUtils.getFile("classpath:sample.xml"));
        EpaperRequestModel epaperRequest = assertDoesNotThrow(() -> XmlUtils.unmarshall(file, EpaperRequestModel.class));

        AppInfoModel appInfo = new AppInfoModel();
        appInfo.setNewspaperName("abb");
        appInfo.setVersion("1.0");

        ScreenInfoModel screenInfo = new ScreenInfoModel();
        screenInfo.setWidth((short) 1280);
        screenInfo.setHeight((short) 752);
        screenInfo.setDpi((short) 160);

        OSInfoModel osInfo = new OSInfoModel();
        osInfo.setName("Browser");
        osInfo.setVersion("1.0");

        DeviceInfoModel deviceInfo = new DeviceInfoModel();
        deviceInfo.setName("Browser");
        deviceInfo.setId("test@comp");
        deviceInfo.setAppInfo(appInfo);
        deviceInfo.setScreenInfo(screenInfo);
        deviceInfo.setOsInfo(osInfo);

        GetPagesModel pages = new GetPagesModel();
        pages.setEditionDefId(11);
        pages.setPublicationDate(LocalDate.parse("2017-06-06", DateTimeFormatter.ISO_DATE));

        EpaperRequestModel temp = new EpaperRequestModel();
        temp.setDeviceInfoModel(deviceInfo);
        temp.setGetPages(pages);

        assertEquals(temp, epaperRequest);
    }

    private static MockMultipartFile createMultipartFile(File file) throws IOException {
        return new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                Files.readAllBytes(file.toPath())
        );
    }
}
