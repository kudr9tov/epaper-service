package com.github.kudr9tov.epaper;

import com.github.kudr9tov.epaper.controllers.NewspaperController;
import com.github.kudr9tov.epaper.repositories.NewspaperRepository;
import com.github.kudr9tov.epaper.repositories.entities.FileModelEmbedded;
import com.github.kudr9tov.epaper.repositories.entities.NewspaperEntity;
import com.github.kudr9tov.epaper.repositories.entities.ScreenInfoEmbedded;
import com.github.kudr9tov.epaper.services.FilesService;
import com.github.kudr9tov.epaper.services.NewspaperService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NewspaperController.class)
public class NewspaperControllerTest {
    @MockBean
    private NewspaperRepository repository;
    @SpyBean
    private NewspaperService newspaperService;
    @MockBean
    private FilesService filesService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_request_random_endpoint_news() throws Exception {
        NewspaperEntity newspaperEntity = createNewspaperEntity("test-controller");
        long id = 1L;
        newspaperEntity.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(newspaperEntity));
        mockMvc.perform(get("/" + UUID.randomUUID())).andExpect(status().isNotFound());
    }

    @Test
    public void should_return_news() throws Exception {
        NewspaperEntity newspaperEntity = createNewspaperEntity("test-controller");
        long id = 1L;
        newspaperEntity.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(newspaperEntity));
        mockMvc.perform(get("/newspapers/{id}", id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.newspaperName").value("test-controller"))
                .andDo(print());
    }

    @Test
    public void should_return_not_found_news() throws Exception {
        NewspaperEntity newspaperEntity = createNewspaperEntity("test-controller");
        long id = 1L;
        newspaperEntity.setId(id);
        when(repository.findById(id)).thenReturn(Optional.empty());
        mockMvc.perform(get("/newspapers/{id}", id)).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.code").value("NEWS_NOT_FOUND"))
                .andExpect(jsonPath("$.error.message").value("News not found"))
                .andExpect(jsonPath("$.requestId").isNotEmpty())
                .andDo(print());
    }

    @Test
    public void should_return_news_page() throws Exception {
        NewspaperEntity newspaperEntity1 = createNewspaperEntity("test-page1");
        NewspaperEntity newspaperEntity2 = createNewspaperEntity("test-page2");
        List<NewspaperEntity> newsList = Lists.newArrayList(newspaperEntity1, newspaperEntity2);
        Page<NewspaperEntity> pagedNews = new PageImpl<>(newsList);

        when(repository.findAll(isA(Pageable.class))).thenReturn(pagedNews);
        mockMvc.perform(get("/newspapers")).andExpect(status().isOk())
                .andExpect(jsonPath("$.entries.size()").value(newsList.size()))
                .andExpect(jsonPath("$.totalNumEntries").value(2))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andDo(print());
    }

    @Test
    public void should_return_news_page_with_filter() throws Exception {
        NewspaperEntity newspaperEntity1 = createNewspaperEntity("test-page1");
        NewspaperEntity newspaperEntity2 = createNewspaperEntity("test-page2");
        List<NewspaperEntity> newsList = Lists.newArrayList(newspaperEntity1, newspaperEntity2);
        Page<NewspaperEntity> pagedNews = new PageImpl<>(newsList);

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        String filter = "test";
        paramsMap.add("newspaperName", filter);

        when(repository.findAllByNewspaperNameContaining(anyString(), isA(Pageable.class))).thenReturn(pagedNews);
        mockMvc.perform(get("/newspapers").params(paramsMap)).andExpect(status().isOk())
                .andExpect(jsonPath("$.entries.size()").value(newsList.size()))
                .andExpect(jsonPath("$.totalNumEntries").value(2))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andDo(print());
    }

    @Test
    public void should_return_news_page_without_filter_check_repository_method() throws Exception {
        when(repository.findAllByNewspaperNameContaining(anyString(), isA(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));
        when(repository.findAll(isA(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/newspapers")).andExpect(status().isOk());

        verify(repository, never()).findAllByNewspaperNameContaining(anyString(), isA(Pageable.class));
        verify(repository).findAll(isA(Pageable.class));

    }

    @Test
    public void should_return_news_page_with_filter_check_repository_method() throws Exception {
        when(repository.findAllByNewspaperNameContaining(anyString(), isA(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));
        when(repository.findAll(isA(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        String filter = "test";
        paramsMap.add("newspaperName", filter);
        mockMvc.perform(get("/newspapers").params(paramsMap)).andExpect(status().isOk());

        verify(repository).findAllByNewspaperNameContaining(anyString(), isA(Pageable.class));
        verify(repository, never()).findAll(isA(Pageable.class));

    }

    @Test
    public void should_delete_news() throws Exception {
        long id = 1L;
        mockMvc.perform(delete("/newspapers/{id}", id)).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void should_upload_news() throws Exception {
        MockMultipartFile file = createMultipartFile(ResourceUtils.getFile("classpath:sample.xml"));
        NewspaperEntity newspaperEntity = createNewspaperEntity("test-controller");
        long id = 1L;
        newspaperEntity.setId(id);
        when(repository.save(isA(NewspaperEntity.class))).thenReturn(newspaperEntity);
        mockMvc.perform(multipart("/newspapers/upload").file(file))
                .andExpect(status().isOk())
                .andDo(print());

        verify(repository).save(isA(NewspaperEntity.class));
        verify(filesService).storeFile(isA(MultipartFile.class));
    }

    @Test
    public void should_upload_wrong_file_news() throws Exception {
        MockMultipartFile file = createMultipartFile(ResourceUtils.getFile("classpath:wrong-sample.xml"));
        NewspaperEntity newspaperEntity = createNewspaperEntity("test-controller");
        long id = 1L;
        newspaperEntity.setId(id);
        when(repository.save(isA(NewspaperEntity.class))).thenReturn(newspaperEntity);
        mockMvc.perform(multipart("/newspapers/upload").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("FILE_NOT_VALID"))
                .andExpect(jsonPath("$.error.message").value("cvc-complex-type.3.2.2: Attribute 'value' is not allowed to appear in element 'screenInfo'."))
                .andExpect(jsonPath("$.requestId").isNotEmpty())
                .andDo(print());

        verify(repository, never()).save(isA(NewspaperEntity.class));
        verify(filesService, never()).storeFile(isA(MultipartFile.class));
    }

    private static NewspaperEntity createNewspaperEntity(String newspaperName) {
        ScreenInfoEmbedded screenInfoEmbedded = ScreenInfoEmbedded.builder()
                .width((short) 1920)
                .height((short) 1024)
                .dpi((short) 123)
                .build();
        LocalDateTime uploadTime = LocalDateTime.now();
        FileModelEmbedded fileModelEmbedded = FileModelEmbedded.builder()
                .upload(uploadTime)
                .originalFilename("test.xml")
                .fileLink("http://localhost:8080/download/81soA_test.xml")
                .build();
        return NewspaperEntity.builder()
                .newspaperName(newspaperName)
                .screenInfo(screenInfoEmbedded)
                .fileModel(fileModelEmbedded)
                .build();
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
