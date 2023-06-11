package com.crewwork.application.crew;

import com.crewwork.domain.crew.Crew;
import com.crewwork.domain.crew.CrewRepository;
import com.crewwork.domain.crew.crewjoin.CrewJoin;
import com.crewwork.domain.crew.crewjoin.CrewJoinRepository;
import com.crewwork.domain.crew.crewmember.CrewMember;
import com.crewwork.domain.crew.crewmember.CrewMemberRepository;
import com.crewwork.domain.member.Member;
import com.crewwork.domain.member.MemberRepository;
import com.crewwork.structure.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class CrewJoinServiceTest {

    @Autowired CrewRepository crewRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired CrewJoinService crewJoinService;
    @Autowired CrewJoinRepository crewJoinRepository;
    @Autowired CrewMemberRepository crewMemberRepository;

    @Test
    @DisplayName("크루 가입 신청 목록 조회")
    void getList() throws Exception {
        // given
        Crew crew = crewRepository.save(Crew.builder().build());
        for (int i = 0; i < 5; i++) {
            Member member = memberRepository.save(Member.builder().build());
            crewJoinRepository.save(CrewJoin.builder()
                    .crew(crew)
                    .member(member).build());
        }

        // when
        List<CrewJoin> crews = crewJoinService.getList(crew.getId());

        // then
        assertThat(crews.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("크루 가입 신청")
    void request() throws Exception {
        // given
        Crew crew = crewRepository.save(Crew.builder().build());
        Member member = memberRepository.save(Member.builder().build());

        // when
        Long crewJoinId = crewJoinService.request(member.getId(), crew.getId());
        CrewJoin findCrewJoin = crewJoinRepository.findById(crewJoinId).get();

        //then
        assertThat(findCrewJoin.getCrew().getId()).isEqualTo(crew.getId());
        assertThat(findCrewJoin.getMember().getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("중복 요청시 BusinessException 에러 발생")
    void requestTwice() throws Exception {
        // given
        Crew crew = crewRepository.save(Crew.builder().build());
        Member member = memberRepository.save(Member.builder().build());

        // when & then
        crewJoinService.request(member.getId(), crew.getId());
        assertThrows(BusinessException.class, () -> crewJoinService.request(member.getId(), crew.getId()));
    }

    @Test
    @DisplayName("이미 가입된 크루에 가입 요청시 BusinessException 에러 발생")
    void requestAlreadyInCrew() throws Exception {
        // given
        Crew crew = crewRepository.save(Crew.builder().build());
        Member member = memberRepository.save(Member.builder().build());
        CrewMember crewMember = crewMemberRepository.save(CrewMember.builder()
                .member(member)
                .crew(crew).build());


        // when & then
        assertThrows(BusinessException.class, () -> crewJoinService.request(member.getId(), crew.getId()));
    }

    @Test
    @DisplayName("크루 가입 신청 승인")
    void accept() throws Exception {
        // given
        Crew crew = crewRepository.save(Crew.builder().build());
        Member member = memberRepository.save(Member.builder().build());
        CrewJoin crewJoin = crewJoinRepository.save(CrewJoin.builder()
                .member(member)
                .crew(crew)
                .build());

        // when
        Long crewMemberId = crewJoinService.accept(crewJoin.getId());
        CrewMember findCrewMember = crewMemberRepository.findById(crewMemberId).get();

        // then
        assertThat(findCrewMember.getCrew().getId()).isEqualTo(crew.getId());
        assertThat(findCrewMember.getMember().getId()).isEqualTo(member.getId());
        assertThat(crewJoinRepository.existsById(crewJoin.getId())).isFalse();
    }

    @Test
    @DisplayName("그루 가입 신청 중복 승인시 BusinessException 에러 발생")
    void acceptTwice() throws Exception {
        // given
        CrewJoin crewJoin = getCrewJoin();

        // when & then
        crewJoinService.accept(crewJoin.getId());
        assertThrows(BusinessException.class, () -> crewJoinService.accept(crewJoin.getId()));
    }

    @Test
    @DisplayName("크루 가입 신청 거절")
    void refuse() throws Exception {
        // given
        CrewJoin crewJoin = getCrewJoin();

        // when
        crewJoinService.refuse(crewJoin.getId());

        // then
        assertThat(crewJoinRepository.existsById(crewJoin.getId())).isFalse();
    }

    @Test
    @DisplayName("크루 가입 신청 중복 거절시 BusinessException 에러 발생")
    void refuseTwice() throws Exception {
        // given
        CrewJoin crewJoin = getCrewJoin();

        // when & then
        crewJoinService.refuse(crewJoin.getId());
        assertThrows(BusinessException.class, () -> crewJoinService.refuse(crewJoin.getId()));
    }

    private CrewJoin getCrewJoin() {
        Crew crew = crewRepository.save(Crew.builder().build());
        Member member = memberRepository.save(Member.builder().build());

        return crewJoinRepository.save(CrewJoin.builder()
                .member(member)
                .crew(crew)
                .build());
    }
}