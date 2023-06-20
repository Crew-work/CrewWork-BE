package com.crewwork.application.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateMemberProjectRequest {

    private String name;
    private String introduce;
    private String link;
}
