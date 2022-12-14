package com.github.kudr9tov.epaper.repositories;

import com.github.kudr9tov.epaper.repositories.entities.NewspaperEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewspaperRepository extends JpaRepository<NewspaperEntity, Long> {

    /**
     * Returns a {@link Page} of {@link NewspaperEntity} meeting the paging restriction provided in the {@link Pageable} object.
     *
     * @param newspaperName must not be {@literal null}.
     * @param pageable the pageable to request a paged result, can be {@link Pageable#unpaged()}, must not be {@literal null}.
     * @return a page of {@link NewspaperEntity}
     */
    Page<NewspaperEntity> findAllByNewspaperNameContaining(String newspaperName, Pageable pageable);

    /**
     * Retrieves an entity by its newspaperName.
     *
     * @param newspaperName must not be {@literal null}.
     * @return the entity with the given newspaperName or {@literal Optional#empty()} if none found.
     */
    Optional<NewspaperEntity> findByNewspaperName(String newspaperName);
}
