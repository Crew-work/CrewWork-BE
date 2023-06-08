package com.crewwork.application.crew;

import com.crewwork.application.common.PageResponse;
import com.crewwork.application.crew.dto.CrewCreateRequest;
import com.crewwork.application.crew.dto.CrewDetailResponse;
import com.crewwork.application.crew.dto.CrewResponse;
import com.crewwork.domain.crew.Crew;
import com.crewwork.domain.crew.CrewRepository;
import com.crewwork.domain.crew.crewmember.CrewMember;
import com.crewwork.domain.crew.crewmember.CrewMemberRepository;
import com.crewwork.domain.crew.crewmember.CrewRole;
import com.crewwork.domain.member.Member;
import com.crewwork.domain.member.MemberRepository;
import com.crewwork.domain.project.CrewProject;
import com.crewwork.domain.project.CrewProjectRepository;
import com.crewwork.structure.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class CrewServiceTest {

    @Autowired CrewService crewService;
    @Autowired CrewRepository crewRepository;
    @Autowired CrewProjectRepository crewProjectRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired CrewMemberRepository crewMemberRepository;

    @Test
    @WithMockCustomUser(username = "user", nickname = "nick")
    void newCrew() throws Exception {
        // given
        MockMultipartFile image = new MockMultipartFile(
                "picture",
                "images.png",
                "png",
                new FileInputStream("D:\\MyProjects\\store\\test-resources\\images.png")
        );

        CrewCreateRequest crewCreateRequest = new CrewCreateRequest("크루 1", "크루 1입니다.", image);
        Long crewId = crewService.newCrew(crewCreateRequest);

        // when
        Crew crew = crewRepository.findById(crewId).get();

        // then
        assertThat(crew.getName()).isEqualTo("크루 1");
        assertThat(crew.getIntroduce()).isEqualTo("크루 1입니다.");
        assertThat(crew.getCreatedBy().getNickname()).isEqualTo("nick");
    }

    @Test
    void getCrews() {
        // given
        for (int i = 1; i <= 10; i++) {
            crewRepository.save(Crew.builder()
                    .name("crew" + i)
                    .introduce("crew" + i +" is nice")
                    .picture("picture")
                    .build());
        }

        // when
        PageResponse<CrewResponse> crews = crewService.getCrews(PageRequest.of(0, 5));

        // then
        assertThat(crews.getTotalElements()).isEqualTo(10);
        assertThat(crews.getTotalPages()).isEqualTo(2);
        assertThat(crews.getContents().size()).isEqualTo(5);
        assertThat(crews.getPageNumber()).isEqualTo(0);
        assertThat(crews.getContents().get(1).getName()).isEqualTo("crew2");
    }

    @Test
    void getCrewDetails() {
        // given
        Crew crew = crewRepository.save(Crew.builder()
                .name("crew")
                .introduce("crew is nice")
                .picture("picture")
                .build());

        for (int i = 1; i <= 5; i++) {
            Member member = memberRepository.save(Member.builder()
                    .nickname("member" + i)
                    .picture("picture")
                    .introduce("i'm member" + i)
                    .occupation("backend")
                    .contact("member" + i + "@crew.work")
                    .techStack("Spring,JAVA")
                    .build());

            crewMemberRepository.save(CrewMember.builder()
                    .crew(crew)
                    .member(member)
                    .role(i==1 ? CrewRole.OWNER : CrewRole.MEMBER)
                    .build());

            crewProjectRepository.save(CrewProject.builder()
                    .crew(crew)
                    .name("crew project " + i)
                    .introduce("crew's project " + i)
                    .link("project link " + i).build());
        }


        // when
        CrewDetailResponse crewDetail = crewService.getCrewDetail(crew.getId());

        // then
        assertThat(crewDetail.getName()).isEqualTo("crew");
        assertThat(crewDetail.getIntroduce()).isEqualTo("crew is nice");
        assertThat(crewDetail.getPicture()).isEqualTo("picture");
        assertThat(crewDetail.getMembers().size()).isEqualTo(5);
        assertThat(crewDetail.getProjects().size()).isEqualTo(5);
    }

    @Test
    void getMyCrews() {
        // given
        Member member = memberRepository.save(Member.builder()
                .nickname("member")
                .picture("picture")
                .introduce("i'm member")
                .occupation("backend")
                .contact("member@crew.work")
                .techStack("Spring,JAVA")
                .build());

        for (int i = 1; i <= 5; i++) {
            Crew crew = crewRepository.save(Crew.builder()
                    .name("crew" + i)
                    .introduce("crew" + i + " is nice")
                    .picture("picture")
                    .build());

            crewMemberRepository.save(CrewMember.builder()
                    .crew(crew)
                    .member(member)
                    .role(CrewRole.OWNER)
                    .build());

            for (int j = 0; j < 5; j++) {
                Member crewMember = memberRepository.save(Member.builder()
                        .nickname("crew member")
                        .picture("picture")
                        .introduce("i'm crew member")
                        .occupation("backend")
                        .contact("member@crew.work")
                        .techStack("Spring,JAVA")
                        .build());

                crewMemberRepository.save(CrewMember.builder()
                        .crew(crew)
                        .member(crewMember)
                        .role(CrewRole.OWNER)
                        .build());
            }
        }

        // when
        PageResponse<CrewResponse> myCrews = crewService.getMyCrews(member.getId(), PageRequest.of(0, 3));

        // then
        assertThat(myCrews.getTotalElements()).isEqualTo(5);
        assertThat(myCrews.getTotalPages()).isEqualTo(2);
        assertThat(myCrews.getContents().size()).isEqualTo(3);
    }
}