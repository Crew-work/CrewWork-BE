package com.crewwork.application.member;

import com.crewwork.application.member.dto.*;
import com.crewwork.domain.member.Member;
import com.crewwork.domain.member.MemberRepository;
import com.crewwork.domain.project.MemberProject;
import com.crewwork.domain.project.MemberProjectRepository;
import com.crewwork.structure.exception.BusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired MemberProjectRepository memberProjectRepository;

    @Test
    @DisplayName("회원 정보 조회")
    void getProfile() throws Exception {
        // given
        Member member = memberRepository.save(Member.builder()
                .nickname("홍길동")
                .picture("image")
                .introduce("자기소개")
                .contact("crewwork@crew.work")
                .techStack("Spring")
                .occupation("백엔드")
                .build());

        // when
        MemberProfileResponse memberProfile = memberService.getProfile(member.getId());

        // then
        assertThat(memberProfile.getNickname()).isEqualTo("홍길동");
        assertThat(memberProfile.getPicture()).isEqualTo("image");
        assertThat(memberProfile.getIntroduce()).isEqualTo("자기소개");
        assertThat(memberProfile.getContact()).isEqualTo("crewwork@crew.work");
        assertThat(memberProfile.getTechStack()).isEqualTo("Spring");
        assertThat(memberProfile.getOccupation()).isEqualTo("백엔드");
    }

    @Test
    @DisplayName("회원 정보 수정")
    void updateProfile() throws Exception {
        // given
        Member member = memberRepository.save(Member.builder().build());

        MockMultipartFile image = new MockMultipartFile(
                "picture",
                "images.png",
                "png",
                new FileInputStream("D:\\MyProjects\\store\\test-resources\\images.png")
        );

        UpdateMemberProfileRequest requestDto = UpdateMemberProfileRequest.builder()
                .nickname("홍길동")
                .picture(image)
                .introduce("자기소개")
                .contact("crewwork@crew.work")
                .techStack("Spring")
                .occupation("백엔드")
                .build();

        // when
        memberService.updateProfile(member.getId(), requestDto);
        Member findMember = memberRepository.findById(member.getId()).get();

        // then
        assertThat(findMember.getNickname()).isEqualTo("홍길동");
        assertThat(findMember.getIntroduce()).isEqualTo("자기소개");
        assertThat(findMember.getContact()).isEqualTo("crewwork@crew.work");
        assertThat(findMember.getTechStack()).isEqualTo("Spring");
        assertThat(findMember.getOccupation()).isEqualTo("백엔드");
    }

    @Test
    @DisplayName("프로젝트 조회")
    void projectList() throws Exception {
        // given
        Member member = memberRepository.save(Member.builder().build());
        for (int i = 0; i < 5; i++) {
            memberProjectRepository.save(MemberProject.builder()
                    .member(member)
                    .name("project")
                    .build());
        }

        // when
        List<MemberProjectsResponse> projects = memberService.projectList(member.getId());

        // then
        assertThat(projects.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("프로젝트 추가")
    void addProject() throws Exception {
        // given
        Member member = memberRepository.save(Member.builder().build());
        AddMemberProjectRequest addMemberProjectRequest = AddMemberProjectRequest.builder()
                .name("project")
                .introduce("project introduce")
                .link("link.com")
                .build();

        // when
        AddMemberProjectResponse addMemberProjectResponse = memberService.addProject(member.getId(), addMemberProjectRequest);
        MemberProject findMemberProject = memberProjectRepository.findById(addMemberProjectResponse.getProjectId()).get();

        // then
        assertThat(findMemberProject.getMember().getId()).isEqualTo(member.getId());
        assertThat(findMemberProject.getName()).isEqualTo("project");
        assertThat(findMemberProject.getIntroduce()).isEqualTo("project introduce");
        assertThat(findMemberProject.getLink()).isEqualTo("link.com");
    }

    @Test
    @DisplayName("프로젝트 수정")
    void updateProject() throws Exception {
        // given
        Member member = memberRepository.save(Member.builder().build());
        MemberProject project = memberProjectRepository.save(MemberProject.builder()
                .member(member)
                .name("project")
                .build());

        UpdateMemberProjectRequest updateMemberProjectRequest = UpdateMemberProjectRequest.builder()
                .name("update project")
                .introduce("update introduce")
                .link("link.update")
                .build();

        // when
        memberService.updateProject(member.getId(), project.getId(), updateMemberProjectRequest);
        MemberProject findMemberProject = memberProjectRepository.findById(project.getId()).get();

        // then
        assertThat(findMemberProject.getMember().getId()).isEqualTo(member.getId());
        assertThat(findMemberProject.getName()).isEqualTo("update project");
        assertThat(findMemberProject.getIntroduce()).isEqualTo("update introduce");
        assertThat(findMemberProject.getLink()).isEqualTo("link.update");
    }



    @Test
    @DisplayName("프로젝트 수정시 프로젝트 소유자가 다르면 BusinessException 발생")
    void updateProjectFail() throws Exception {
        // given
        Member member = memberRepository.save(Member.builder().build());
        MemberProject project = memberProjectRepository.save(MemberProject.builder()
                .member(member)
                .name("project")
                .build());

        UpdateMemberProjectRequest updateMemberProjectRequest = UpdateMemberProjectRequest.builder()
                .name("update project")
                .introduce("update introduce")
                .link("link.update")
                .build();

        // when & then
        assertThrows(BusinessException.class, () -> memberService.updateProject(member.getId() + 1, project.getId(), updateMemberProjectRequest));
    }

    @Test
    @DisplayName("프로젝트 삭제")
    void deleteProject() throws Exception {
        // given
        Member member = memberRepository.save(Member.builder().build());
        MemberProject project = memberProjectRepository.save(MemberProject.builder()
                .member(member)
                .name("project")
                .build());

        // when
        memberService.deleteProject(member.getId(), project.getId());

        // then
        assertThat(memberProjectRepository.findById(project.getId())).isEmpty();
    }

    @Test
    @DisplayName("프로젝트 삭제시 프로젝트 소유자가 다르면 BusinessException 발생")
    void deleteProjectFail() throws Exception {
        // given
        Member member = memberRepository.save(Member.builder().build());
        MemberProject project = memberProjectRepository.save(MemberProject.builder()
                .member(member)
                .name("project")
                .build());

        // when & then
        assertThrows(BusinessException.class, () -> memberService.deleteProject(member.getId() + 1, project.getId()));

    }

}