package com.crewwork.application.login;

import lombok.Data;

@Data
public class LoginCallbackRequest {
    private String accessToken;
    private String refreshToken;
}
