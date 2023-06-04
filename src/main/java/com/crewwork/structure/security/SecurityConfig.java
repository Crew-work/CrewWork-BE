package com.crewwork.structure.security;

import com.crewwork.domain.member.MemberRepository;
import com.crewwork.structure.security.jwtauth.JwtAuthorizationFilter;
import com.crewwork.structure.security.jwtauth.JwtTokenProvider;
import com.crewwork.structure.security.oauth.OAuth2AuthenticationSuccessHandler;
import com.crewwork.structure.security.oauth.PrincipalOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PrincipalOAuth2UserService principalOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                // 세션 로그인 비활성화
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // url 접근 권한 설정
                .and()
                .cors(Customizer.withDefaults())
                .authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()

//                .and()
//                .exceptionHandling()
//                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())

                // 일반 로그인 설정
                .and()
                .formLogin().disable()

                // JWT 설정
                .httpBasic().disable()
                .apply(new MyCustomDsl())

                // OAuth2 로그인 설정
                .and()
                .oauth2Login()
//                .loginPage("/loginForm")
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .userInfoEndpoint()
                .userService(principalOAuth2UserService);

        // 로그아웃 설정
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(((request, response, authentication) -> {
                    jwtTokenProvider.removeRefreshToken(request);
                    response.setStatus(HttpServletResponse.SC_OK);
                }))
                .deleteCookies("refreshToken");

        return http.build();
    }


    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, memberRepository, jwtTokenProvider));
        }
    }
}
