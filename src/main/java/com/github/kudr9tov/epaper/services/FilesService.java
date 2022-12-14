package com.github.kudr9tov.epaper.services;

import com.github.kudr9tov.epaper.exceptions.FileNotFoundException;
import com.github.kudr9tov.epaper.exceptions.FileUploadException;
import com.github.kudr9tov.epaper.repositories.entities.NewspaperEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.stream.Stream;

@Service
public class FilesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilesService.class);

    private final String uploadFolder;

    @Value("${service.download.baseUrl}")
    private String downloadUrl;

    /**
     * Initialization path for save files
     *
     * @param uploadFolder of {@link String} location where files can be saved.
     */
    public FilesService(@Value("${service.upload.folder}") String uploadFolder) {
        this.uploadFolder = uploadFolder;
        File directory = new File(uploadFolder);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Returns a link designed for download file from files system
     *
     * @param file of {@link MultipartFile} must not be {@literal null}.
     * @return {@link String}
     */
    public String storeFile(MultipartFile file) {
        try {
            String uploadedFileName = RandomStringUtils.randomAlphanumeric(5) + "_" + file.getOriginalFilename();
            final String safeFileName = getSafeFileName(uploadedFileName);
            String path = MessageFormat.format("{0}/{1}", uploadFolder, safeFileName);
            Files.copy(file.getInputStream(), Paths.get(path));
            return MessageFormat.format("{0}/{1}", downloadUrl, safeFileName);
        } catch (IOException e) {
            throw new FileUploadException(e);
        }
    }

    /**
     * Returns a file which located in application file system
     *
     * @param fileName of {@link String}.
     * @return {@link File}
     */
    public File getFile(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return null;
        }
        try (Stream<Path> stream = Files.list(Paths.get(uploadFolder))) {
            return stream
                    .filter(f -> StringUtils.equals(f.getFileName().toString(), fileName))
                    .findFirst()
                    .map(Path::toFile)
                    .orElseThrow(FileNotFoundException::new);
        } catch (IOException e) {
            LOGGER.error("Can't scrapped files from directory {}", uploadFolder, e);
        }
        throw new FileNotFoundException();
    }

    /**
     * Deleting file from application file system if it exist
     *
     * @param fileName of {@link String}.
     */
    public void checkFileAndDelete(String fileName) {
        try {
            File file = getFile(fileName);
            if (!file.delete()) {
                LOGGER.warn("Something was wrong. File wasn't deleted");
            }
        } catch (Exception e) {
            LOGGER.warn("File not found or was deleted");
        }

    }

    private String getSafeFileName(String fileName) {
        return fileName.replaceAll("[%#?]", StringUtils.EMPTY);
    }

}
