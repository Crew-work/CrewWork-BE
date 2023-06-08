package com.crewwork.application.crew.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class CrewCreateRequest {

    private String name;
    private String introduce;
    private MultipartFile picture;

    @Builder
    public CrewCreateRequest(String name, String introduce, MultipartFile picture) {
        this.name = name;
        this.introduce = introduce;
        this.picture = picture;
    }
}
