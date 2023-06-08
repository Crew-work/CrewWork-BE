package com.crewwork.structure.exception;

public enum ErrorCode {

    CREW_NOT_FOUND(404, "크루를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    ALREADY_CREW_JOIN_APPLY(400, "이미 가입 신청된 크루입니다.");

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
