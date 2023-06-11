package com.crewwork.application.crew.dto;

import com.crewwork.domain.crew.Crew;
import lombok.Getter;

@Getter
public class CrewInfoResponse {
    private String name;
    private String introduce;
    private String picture;

    public CrewInfoResponse(Crew crew) {
        this.name = crew.getName();
        this.introduce = crew.getIntroduce();
        this.picture = crew.getPicture();
    }
}
