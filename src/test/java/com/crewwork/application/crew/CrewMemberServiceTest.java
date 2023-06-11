package com.crewwork.application.crew;

import com.crewwork.domain.crew.Crew;
import com.crewwork.domain.crew.CrewRepository;
import com.crewwork.domain.crew.crewmember.CrewMember;
import com.crewwork.domain.crew.crewmember.CrewMemberRepository;
import com.crewwork.domain.crew.crewmember.CrewRole;
import com.crewwork.domain.member.Member;
import com.crewwork.domain.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class CrewMemberServiceTest {

    @Autowired CrewMemberRepository crewMemberRepository;
    @Autowired CrewMemberService crewMemberService;
    @Autowired MemberRepository memberRepository;
    @Autowired CrewRepository crewRepository;

    @Test
    @DisplayName("크루 멤버 조회")
    void getList() throws Exception {
        // given
        Crew crew = crewRepository.save(Crew.builder().build());
        for (int i = 0; i < 5; i++) {
            Member member = memberRepository.save(Member.builder().build());
            crewMemberRepository.save(CrewMember.builder()
                    .crew(crew)
                    .member(member).build());
        }

        // when
        List<CrewMember> crewMembers = crewMemberService.getList(crew.getId());

        // then
        assertThat(crewMembers.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("크루원 퇴출")
    void kick() throws Exception {
        // given
        Crew crew = crewRepository.save(Crew.builder().build());
        Member member = memberRepository.save(Member.builder().build());
        crewMemberRepository.save(CrewMember.builder()
                .crew(crew)
                .member(member).build());

        // when
        crewMemberService.kick(crew.getId(), member.getId());

        // then
    }

    @Test
    @DisplayName("크루장 위임")
    void delegate() throws Exception {
        // given
        Crew crew = crewRepository.save(Crew.builder().build());
        Member member = memberRepository.save(Member.builder().build());
        Member owner = memberRepository.save(Member.builder().build());

        CrewMember crewMember = crewMemberRepository.save(CrewMember.builder()
                .crew(crew)
                .member(member)
                .role(CrewRole.MEMBER).build());

        CrewMember crewOwner = crewMemberRepository.save(CrewMember.builder()
                .crew(crew)
                .member(owner)
                .role(CrewRole.OWNER).build());

        // when
        crewMemberService.delegate(crew.getId(), member.getId());

        // then
        assertThat(crewMember.getRole()).isEqualTo(CrewRole.OWNER);
        assertThat(crewOwner.getRole()).isEqualTo(CrewRole.MEMBER);
    }
}