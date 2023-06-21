package com.crewwork.application.login;

import com.crewwork.application.login.dto.LoginCallbackRequest;
import com.crewwork.application.login.dto.LoginCallbackResponse;
import com.crewwork.application.login.dto.ReIssuanceTokenResponse;
import com.crewwork.structure.security.jwtauth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @ResponseBody
    @PostMapping("/login-callback")
    public LoginCallbackResponse loginCallbackDto(@RequestBody LoginCallbackRequest loginCallbackRequest, HttpServletResponse response) throws IOException {

        jwtTokenProvider.verify(loginCallbackRequest.getAccessToken());
        jwtTokenProvider.verify(loginCallbackRequest.getRefreshToken());

        jwtTokenProvider.saveRefreshToken(loginCallbackRequest.getRefreshToken());

        Cookie refreshTokenCookie = new Cookie("refreshToken", loginCallbackRequest.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        response.addCookie(refreshTokenCookie);

        return new LoginCallbackResponse(loginCallbackRequest.getAccessToken());
    }

    @ResponseBody
    @PostMapping("/reissuance-token")
    public ReIssuanceTokenResponse reIssuanceToken(@CookieValue("refreshToken") String refreshToken) {
        String newAccessToken = jwtTokenProvider.reIssuanceAccessToken(refreshToken);

        return new ReIssuanceTokenResponse(newAccessToken);
    }
}
