package com.github.kudr9tov.epaper.controllers.payload.responses;

import lombok.Builder;
import lombok.Getter;

import java.util.Collection;

@Builder
@Getter
public class PageResponse<T> {
    private Collection<T> entries;
    private int page;
    private int totalPages;
    private long totalNumEntries;
}
