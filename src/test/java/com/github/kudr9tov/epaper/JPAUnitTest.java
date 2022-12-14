package com.github.kudr9tov.epaper;

import com.github.kudr9tov.epaper.repositories.NewspaperRepository;
import com.github.kudr9tov.epaper.repositories.entities.FileModelEmbedded;
import com.github.kudr9tov.epaper.repositories.entities.NewspaperEntity;
import com.github.kudr9tov.epaper.repositories.entities.ScreenInfoEmbedded;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JPAUnitTest {
    @Autowired
    NewspaperRepository repository;

    @Test
    @Order(1)
    public void should_find_no_news_if_repository_is_empty() {
        Iterable<NewspaperEntity> tutorials = repository.findAll();
        assertThat(tutorials).isEmpty();
    }

    @Test
    @Order(2)
    public void should_store_a_news() {
        NewspaperEntity test = createNewspaperEntity("test");
        NewspaperEntity entity = repository.save(test);

        assertThat(entity).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(entity).hasFieldOrPropertyWithValue("newspaperName", test.getNewspaperName());
    }

    @Test
    public void should_find_all_news() {
        NewspaperEntity entity1 = createNewspaperEntity("entity1");
        repository.save(entity1);
        NewspaperEntity entity2 = createNewspaperEntity("entity2");
        repository.save(entity2);
        NewspaperEntity entity3 = createNewspaperEntity("entity3");
        repository.save(entity3);

        Iterable<NewspaperEntity> tutorials = repository.findAll();
        assertThat(tutorials).hasSize(3).contains(entity1, entity2, entity3);
    }

    @Test
    public void should_find_news_by_id() {
        repository.save(createNewspaperEntity("entity1"));
        NewspaperEntity savedEntity2 = repository.save(createNewspaperEntity("entity2"));

        Optional<NewspaperEntity> foundEntity = repository.findById(savedEntity2.getId());

        assertThat(foundEntity).isPresent();
        assertThat(foundEntity.get()).isEqualTo(savedEntity2);
    }

    @Test
    public void should_find_news_by_newspaperName_containing_string() {
        NewspaperEntity savedEntity1 = repository.save(createNewspaperEntity("test"));
        NewspaperEntity savedEntity2 = repository.save(createNewspaperEntity("realnews"));
        NewspaperEntity savedEntity3 = repository.save(createNewspaperEntity("lifenews"));

        Iterable<NewspaperEntity> news = repository.findAllByNewspaperNameContaining("news", Pageable.ofSize(3));

        assertThat(news).hasSize(2).contains(savedEntity2, savedEntity3);
    }

    @Test
    public void should_delete_news_by_id() {
        NewspaperEntity entity1 = createNewspaperEntity("entity1");
        NewspaperEntity savedEntity1 = repository.save(entity1);
        NewspaperEntity entity2 = createNewspaperEntity("entity2");
        NewspaperEntity savedEntity2 = repository.save(entity2);

        repository.deleteById(savedEntity2.getId());
        Iterable<NewspaperEntity> tutorials = repository.findAll();

        assertThat(tutorials).hasSize(1).contains(savedEntity1);
    }

    @Test
    public void should_delete_all_news() {
        repository.save(createNewspaperEntity("entity1"));
        repository.save(createNewspaperEntity("entity2"));
        repository.save(createNewspaperEntity("entity3"));
        repository.save(createNewspaperEntity("entity4"));

        repository.deleteAll();

        assertThat(repository.findAll()).isEmpty();
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
}