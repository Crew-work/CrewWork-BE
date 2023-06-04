package com.crewwork.structure.security.oauth;

import com.crewwork.structure.security.PrincipalDetails;
import com.crewwork.structure.security.jwtauth.JwtToken;
import com.crewwork.structure.security.jwtauth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        JwtToken token = jwtTokenProvider.createToken("Crew Work", principalDetails.getMember().getId());

//        response.addHeader(JwtTokenProvider.ACCESSTOKEN_HEADER, token.getAccessToken());
//        response.addHeader(JwtTokenProvider.REFRESHTOKEN_HEADER, token.getRefreshToken());

        response.sendRedirect("http://localhost:3000/login-callback" +
                "?accessToken=" + token.getAccessToken() +
                "&refreshToken=" + token.getRefreshToken());
    }
}
