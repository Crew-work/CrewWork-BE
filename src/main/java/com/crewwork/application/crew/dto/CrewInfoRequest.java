package com.crewwork.application.crew.dto;

import com.crewwork.domain.crew.Crew;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CrewInfoRequest {
    private String name;
    private String introduce;
    private String picture;
}
