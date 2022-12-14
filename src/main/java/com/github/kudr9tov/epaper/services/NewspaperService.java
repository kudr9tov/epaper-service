package com.github.kudr9tov.epaper.services;

import com.github.kudr9tov.epaper.exceptions.NewspaperNotFoundException;
import com.github.kudr9tov.epaper.repositories.NewspaperRepository;
import com.github.kudr9tov.epaper.repositories.entities.FileModelEmbedded;
import com.github.kudr9tov.epaper.repositories.entities.NewspaperEntity;
import com.github.kudr9tov.epaper.repositories.entities.ScreenInfoEmbedded;
import com.github.kudr9tov.epaper.services.models.AppInfoModel;
import com.github.kudr9tov.epaper.services.models.DeviceInfoModel;
import com.github.kudr9tov.epaper.services.models.EpaperRequestModel;
import com.github.kudr9tov.epaper.services.models.ScreenInfoModel;
import com.github.kudr9tov.epaper.utils.XmlUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class NewspaperService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewspaperService.class);
    private final FilesService filesService;
    private final NewspaperRepository newspaperRepository;

    @Autowired
    public NewspaperService(final FilesService filesService,
                            final NewspaperRepository newspaperRepository) {
        this.filesService = filesService;
        this.newspaperRepository = newspaperRepository;
    }

    /**
     * Returns a {@link Page} of entities meeting the paging restriction provided in the {@link Pageable} object.
     *
     * @param newspaperName can be {@literal null}. Used for filtration
     * @return a page of {@link NewspaperEntity}
     */
    public Page<NewspaperEntity> getNewsPage(String newspaperName,
                                             int page,
                                             int size,
                                             String[] sort) {
        List<Order> orders = Lists.newArrayList();
        for (String sortOrder : sort) {
            String[] _sort = sortOrder.split(":");
            orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
        }
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));
        if (StringUtils.isBlank(newspaperName)) {
            return newspaperRepository.findAll(pagingSort);
        }
        return newspaperRepository.findAllByNewspaperNameContaining(newspaperName, pagingSort);
    }

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id.
     * @throws NewspaperNotFoundException if object didn't find in storage.
     */
    public NewspaperEntity getNews(@NotNull Long id) {
        return newspaperRepository.findById(id)
                .orElseThrow(NewspaperNotFoundException::new);
    }

    /**
     * Retrieves an entity by its id.
     *
     * @param multipartFile of {@link MultipartFile} must not be {@literal null}.
     * @return the entity.
     * @throws SAXException if file does not match the XSD schema.
     */
    public NewspaperEntity uploadNews(MultipartFile multipartFile) {
        XmlUtils.validate(multipartFile);
        EpaperRequestModel epaperRequestModel = XmlUtils.unmarshall(multipartFile, EpaperRequestModel.class);
        NewspaperEntity newspaperEntity = transformToEntity(epaperRequestModel);
        String downloadLink = filesService.storeFile(multipartFile);
        FileModelEmbedded fileModelEmbedded = FileModelEmbedded.builder()
                .upload(LocalDateTime.now())
                .fileLink(downloadLink)
                .originalFilename(multipartFile.getOriginalFilename())
                .build();
        newspaperEntity.setFileModel(fileModelEmbedded);
        try {
            newspaperRepository.findByNewspaperName(newspaperEntity.getNewspaperName())
                    .ifPresent(foundEntity -> newspaperEntity.setId(foundEntity.getId()));
            return newspaperRepository.save(newspaperEntity);
        } catch (Exception e) {
            filesService.checkFileAndDelete(getFileName(downloadLink));
            throw e;
        }
    }


    /**
     * Deletes the entity with the given id and deletes the uploaded file for this entity if file exist.
     * <p>
     * If the entity is not found in the persistence store it is silently ignored.
     *
     * @param id must not be {@literal null}.
     */
    public void removeNews(@NotNull Long id) {
        Optional<NewspaperEntity> foundEntity = newspaperRepository.findById(id);
        if (foundEntity.isPresent()) {
            NewspaperEntity entity = foundEntity.get();
            newspaperRepository.delete(entity);
            filesService.checkFileAndDelete(getFileName(entity.getFileModel().getFileLink()));
        }
    }

    private String getFileName(String fileLink) {
        return Optional.ofNullable(fileLink)
                .map(str -> StringUtils.substringAfterLast(str, "/"))
                .orElse(null);
    }

    private NewspaperEntity transformToEntity(@NotNull EpaperRequestModel epaperRequestModel) {
        DeviceInfoModel deviceInfoModel = epaperRequestModel.getDeviceInfoModel();
        if (Objects.isNull(deviceInfoModel)) {
            throw new IllegalArgumentException("Required fields doesn't presented in a file");
        }
        ScreenInfoModel screenInfoModel = deviceInfoModel.getScreenInfo();
        AppInfoModel appInfoModel = deviceInfoModel.getAppInfo();
        if (Objects.isNull(screenInfoModel) || Objects.isNull(appInfoModel)) {
            throw new IllegalArgumentException("Required fields doesn't presented in a file");
        }

        String newspaperName = Optional.of(appInfoModel.getNewspaperName()).orElseThrow(IllegalArgumentException::new);

        ScreenInfoEmbedded screenInfoEmbedded = ScreenInfoEmbedded.builder()
                .width(screenInfoModel.getWidth())
                .height(screenInfoModel.getHeight())
                .dpi(screenInfoModel.getDpi())
                .build();

        return NewspaperEntity.builder()
                .newspaperName(newspaperName)
                .screenInfo(screenInfoEmbedded)
                .build();
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }
}
