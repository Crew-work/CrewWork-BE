package com.crewwork.application.crew.dto;

import com.crewwork.domain.crew.crewjoin.CrewJoinBoard;
import lombok.Getter;

@Getter
public class JoinBoardCreateResponse {

    private Long id;

    public JoinBoardCreateResponse(CrewJoinBoard crewJoinBoard) {
        this.id = crewJoinBoard.getId();
    }
}
