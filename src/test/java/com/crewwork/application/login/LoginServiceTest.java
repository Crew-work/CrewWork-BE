package com.crewwork.application.login;

import com.crewwork.application.login.dto.WriteMemberInfoRequest;
import com.crewwork.domain.member.Member;
import com.crewwork.domain.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class LoginServiceTest {

    @Autowired LoginService loginService;
    @Autowired MemberRepository memberRepository;

    @Test
    @DisplayName("회원정보 작성")
    void writeMemberInfo() throws Exception {
        // given
        Member member = memberRepository.save(Member.builder()
                .nickname("nickname")
                .techStack("")
                .occupation("")
                .contact("")
                .introduce("introduce")
                .build());

        WriteMemberInfoRequest writeMemberInfoRequest = WriteMemberInfoRequest.builder()
                .contact("crewwork@crew.work")
                .techStack("Spring")
                .occupation("백엔드")
                .build();

        // when
        loginService.writeMemberInfo(member.getId(), writeMemberInfoRequest);
        Member findMember = memberRepository.findById(member.getId()).get();

        // then
        assertThat(findMember.getNickname()).isEqualTo("nickname");
        assertThat(findMember.getIntroduce()).isEqualTo("introduce");
        assertThat(findMember.getTechStack()).isEqualTo("Spring");
        assertThat(findMember.getOccupation()).isEqualTo("백엔드");
        assertThat(findMember.getContact()).isEqualTo("crewwork@crew.work");
    }
}