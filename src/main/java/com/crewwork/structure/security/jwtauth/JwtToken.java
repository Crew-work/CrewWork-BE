package com.crewwork.structure.security.jwtauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public class JwtToken {

    private String accessToken;
    private String refreshToken;

}
