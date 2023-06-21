package com.crewwork.application.crew.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinBoardCreateRequest {

    private String title;
    private String content;
    private Long crewId;

}
