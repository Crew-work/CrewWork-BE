package com.crewwork.application.crew.admin.dto;

import com.crewwork.domain.project.CrewProject;
import lombok.Getter;

@Getter
public class CrewProjectResponse {

    private Long projectId;
    private String name;
    private String introduce;
    private String link;

    public CrewProjectResponse(CrewProject crewProject) {
        this.projectId = crewProject.getId();
        this.name = crewProject.getName();
        this.introduce = crewProject.getIntroduce();
        this.link = crewProject.getLink();
    }

}
