package com.crewwork.structure.exception;

public enum ErrorCode {

    CREW_NOT_FOUND(404, "크루를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    CREWJOIN_NOT_FOUND(404, "가입 신청 내역을 찾을 수 없습니다."),
    CREWMEMBER_NOT_FOUND(404, "해당 크루의 사용자를 찾을 수 없습니다."),
    PROJECT_NOT_FOUND(404, "프로젝트를 찾을 수 없습니다."),
    JOINBOARD_NOT_FOUND(404, "모집글을 찾을 수 없습니다."),

    ALREADY_CREW_JOIN_APPLY(400, "이미 가입 신청된 크루입니다."),
    ALREADY_CREW_MEMBER(400, "이미 소속된 크루입니다."),

    CANNOT_KICK_OWNER(400, "크루장을 추방할 수 없습니다."),
    CANNOT_UPDATE_PROJECT(400, "프로젝트 정보를 수정할 수 없습니다."),
    CANNOT_UPDATE_JOINBOARD(400, "모집글을 수정할 수 없습니다."),
    CANNOT_DELETE_JOINBOARD(400, "모집글을 삭제할 수 없습니다.");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }
}
