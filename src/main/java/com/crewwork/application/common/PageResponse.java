package com.crewwork.application.common;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PageResponse<T> {

    private List<T> contents;
    private boolean first;
    private boolean last;
    private long totalElements;
    private long totalPages;
    private long pageNumber;
}
