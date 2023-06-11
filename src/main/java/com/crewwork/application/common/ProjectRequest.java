package com.crewwork.application.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectRequest {

    private String name;
    private String introduce;
    private String link;
}
