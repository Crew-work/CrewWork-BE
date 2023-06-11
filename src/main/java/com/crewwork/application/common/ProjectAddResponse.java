package com.crewwork.application.common;

import lombok.Getter;

@Getter
public class ProjectAddResponse {

    private Long projectId;

    public ProjectAddResponse(Long projectId) {
        this.projectId = projectId;
    }
}
