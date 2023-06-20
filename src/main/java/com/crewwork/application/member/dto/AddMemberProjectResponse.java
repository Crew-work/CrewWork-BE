package com.crewwork.application.member.dto;

import com.crewwork.domain.project.MemberProject;
import lombok.Getter;

@Getter
public class AddMemberProjectResponse {

    private Long projectId;

    public AddMemberProjectResponse(MemberProject memberProject) {
        this.projectId = memberProject.getId();
    }
}
