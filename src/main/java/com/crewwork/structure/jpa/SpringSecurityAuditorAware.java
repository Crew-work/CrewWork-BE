package com.crewwork.structure.jpa;

import com.crewwork.domain.member.Member;
import com.crewwork.structure.security.PrincipalDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<Member> {

    @Override
    public Optional<Member> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            return Optional.empty();
        }

        Member member = ((PrincipalDetails) authentication.getPrincipal()).getMember();
        return Optional.ofNullable(member);
    }
}
