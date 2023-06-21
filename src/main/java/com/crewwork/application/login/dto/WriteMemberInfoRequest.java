package com.crewwork.application.login.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WriteMemberInfoRequest {

    private String occupation;
    private String techStack;
    private String contact;

}
