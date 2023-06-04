package com.crewwork.application.login;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginCallbackResponse {
    private String accessToken;
}
