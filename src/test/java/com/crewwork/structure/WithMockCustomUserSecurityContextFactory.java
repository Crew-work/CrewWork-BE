package com.crewwork.structure;

import com.crewwork.domain.member.Member;
import com.crewwork.domain.member.MemberRepository;
import com.crewwork.structure.security.PrincipalDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;


public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public WithMockCustomUserSecurityContextFactory(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Member member = memberRepository.findByUsername(annotation.username()).orElseGet(() ->
                memberRepository.save(Member.builder()
                        .provider("test provider")
                        .providerId("test1234")
                        .username(annotation.username())
                        .password(passwordEncoder.encode("test password"))
                        .contact("test@test.com")
                        .picture("test picture")
                        .role("ROLE_USER")
                        .nickname(annotation.nickname())
                        .build())
        );

        PrincipalDetails principalDetails = new PrincipalDetails(member);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

        context.setAuthentication(authentication);
        return context;
    }
}
