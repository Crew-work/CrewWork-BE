package com.crewwork.application.crew.dto;

import lombok.Getter;

@Getter
public class CrewCreateResponse {
    private Long crewId;

    public CrewCreateResponse(Long crewId) {
        this.crewId = crewId;
    }
}
