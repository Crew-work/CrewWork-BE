package com.crewwork.application.crew;

import com.crewwork.domain.crew.Crew;
import com.crewwork.domain.crew.CrewRepository;
import com.crewwork.domain.member.Member;
import com.crewwork.domain.member.MemberRepository;
import com.crewwork.structure.exception.BusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class CrewJoinServiceTest {

    @Autowired CrewRepository crewRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired CrewJoinService crewJoinService;

    @Test
    void apply() throws Exception {
        // given
        Crew crew = crewRepository.save(Crew.builder().build());
        Member member = memberRepository.save(Member.builder().build());

        // when & then
        crewJoinService.apply(member.getId(), crew.getId());
    }

    @Test
    void applyTwice() throws Exception {
        // given
        Crew crew = crewRepository.save(Crew.builder().build());
        Member member = memberRepository.save(Member.builder().build());
        crewJoinService.apply(member.getId(), crew.getId());

        // when & then
        Assertions.assertThrows(BusinessException.class, () -> crewJoinService.apply(member.getId(), crew.getId()));
    }
}