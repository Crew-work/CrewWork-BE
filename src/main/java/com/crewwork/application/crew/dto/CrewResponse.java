package com.crewwork.application.crew.dto;

import com.crewwork.domain.crew.Crew;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CrewResponse {
    private final Long id;
    private final String name;
    private final String introduce;
    private final String picture;

    public CrewResponse(Crew crew) {
        this.id = crew.getId();
        this.name = crew.getName();
        this.introduce = crew.getIntroduce();
        this.picture = crew.getPicture();
    }
}
