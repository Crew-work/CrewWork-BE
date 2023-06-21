package com.crewwork.application.crew.dto;

import com.crewwork.domain.crew.crewjoin.CrewJoinBoard;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class JoinBoardDetailResponse {

    private Long crewId;
    private String title;
    private String content;
    private String crewName;
    private LocalDateTime createdDate;

    public JoinBoardDetailResponse(CrewJoinBoard crewJoinBoard) {
        this.crewId = crewJoinBoard.getCrew().getId();
        this.title = crewJoinBoard.getTitle();
        this.content = crewJoinBoard.getContent();
        this.crewName = crewJoinBoard.getCrew().getName();
        this.createdDate = crewJoinBoard.getCreatedDate();
    }

}
