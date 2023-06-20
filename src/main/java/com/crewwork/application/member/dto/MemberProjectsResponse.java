package com.crewwork.application.member.dto;

import com.crewwork.domain.project.MemberProject;
import lombok.Getter;

@Getter
public class MemberProjectsResponse {

    private Long projectId;
    private String name;
    private String introduce;
    private String link;

    public MemberProjectsResponse(MemberProject memberProject) {
        this.projectId = memberProject.getId();
        this.name = memberProject.getName();
        this.introduce = memberProject.getIntroduce();
        this.link = memberProject.getLink();
    }
}
