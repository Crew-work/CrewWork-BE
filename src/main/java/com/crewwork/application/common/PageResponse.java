package com.crewwork.application.common;

import com.crewwork.application.crew.dto.CrewResponse;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse<T> {

    private List<T> contents;
    private boolean first;
    private boolean last;
    private long totalElements;
    private long totalPages;
    private long pageNumber;

    public PageResponse(Page page) {
        this.first = page.isFirst();
        this.last = page.isLast();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.pageNumber = page.getNumber();
        this.contents = page.getContent();
    }
}
