package com.crewwork.application.crew.admin.dto;

import com.crewwork.domain.crew.Crew;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class CrewInfoRequest {
    private String name;
    private String introduce;
    private MultipartFile picture;

}
