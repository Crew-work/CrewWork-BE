package com.crewwork.structure.security.jwtauth;

import com.crewwork.domain.member.Member;
import com.crewwork.domain.member.MemberRepository;
import com.crewwork.structure.security.PrincipalDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

       if(request.getMethod().equals("POST") && request.getRequestURI().equals("/reissuance-token")) {
           chain.doFilter(request, response);
           return;
       }

       if(request.getRequestURI().equals("/logout")) {
           chain.doFilter(request, response);
           return;
       }

        String accessTokenHeader = request.getHeader(JwtTokenProvider.HEADER_STRING);
        if (accessTokenHeader == null || !accessTokenHeader.startsWith(JwtTokenProvider.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String accessToken = accessTokenHeader.replace(JwtTokenProvider.TOKEN_PREFIX, "");
        try {
            Long memberId = jwtTokenProvider.getMemberIdFromToken(accessToken);
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다"));

            PrincipalDetails principalDetails = new PrincipalDetails(member);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        } catch (JwtRuntimeException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }

    }
}
