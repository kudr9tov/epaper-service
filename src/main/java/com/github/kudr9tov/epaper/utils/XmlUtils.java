package com.github.kudr9tov.epaper.utils;


import com.github.kudr9tov.epaper.exceptions.GeneralException;
import com.github.kudr9tov.epaper.exceptions.IllegalXmlFileStructureException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.InputStream;

public class XmlUtils {
    public static final String CLASSPATH_SCHEMA_XSD = "classpath:schema.xsd";

    /**
     * Validates uploaded file by XSD schema.
     *
     * @param multipartFile must not be {@literal null}.
     */
    public static void validate(MultipartFile multipartFile) {
        try (InputStream in = multipartFile.getInputStream();
             InputStream xsdIn = ResourceUtils.getURL(CLASSPATH_SCHEMA_XSD).openStream()) {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(xsdIn));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(in));
        } catch (SAXException e) {
            throw new IllegalXmlFileStructureException(e.getMessage(), e);
        } catch (Exception e) {
            throw new GeneralException(e.getMessage(), e);
        }
    }

    /**
     * Deserialization process from xml file to jaba object.
     *
     * @param multipartFile must not be {@literal null}.
     * @param clazz must not be {@literal null}. Type of deserialization object.
     */
    public static <T> T unmarshall(MultipartFile multipartFile, Class<T> clazz) {
        try (InputStream in = multipartFile.getInputStream()) {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Object object = context.createUnmarshaller()
                    .unmarshal(in);
            return clazz.cast(object);
        } catch (JAXBException e) {
            throw new IllegalXmlFileStructureException(e.getMessage(), e);
        } catch (Exception e) {
            throw new GeneralException(e.getMessage(), e);
        }
    }
}
