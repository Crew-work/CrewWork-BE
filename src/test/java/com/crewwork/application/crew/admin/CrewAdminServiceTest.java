package com.crewwork.application.crew.admin;

import com.crewwork.application.common.ProjectRequest;
import com.crewwork.application.crew.admin.dto.CrewInfoRequest;
import com.crewwork.application.crew.admin.dto.CrewInfoResponse;
import com.crewwork.application.crew.admin.dto.CrewProjectResponse;
import com.crewwork.domain.crew.Crew;
import com.crewwork.domain.crew.CrewRepository;
import com.crewwork.domain.project.CrewProject;
import com.crewwork.domain.project.CrewProjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CrewAdminServiceTest {

    @Autowired CrewAdminService crewAdminService;
    @Autowired CrewRepository crewRepository;
    @Autowired CrewProjectRepository crewProjectRepository;


    @Test
    @DisplayName("크루 정보 조회")
    void crewInfo() throws Exception {
        // given
        Crew crew = crewRepository.save(Crew.builder()
                .name("crew")
                .introduce("crew introduce")
                .picture("picture")
                .build());

        // when
        CrewInfoResponse crewInfoResponse = crewAdminService.crewInfo(crew.getId());

        // then
        assertThat(crewInfoResponse.getName()).isEqualTo("crew");
        assertThat(crewInfoResponse.getIntroduce()).isEqualTo("crew introduce");
        assertThat(crewInfoResponse.getPicture()).isEqualTo("picture");
    }

    @Test
    @DisplayName("크루 정보 수정")
    void crewInfoUpdate() throws Exception {
        // given
        Crew crew = crewRepository.save(Crew.builder()
                .name("crew")
                .introduce("crew introduce")
                .picture("picture")
                .build());


        MockMultipartFile image = new MockMultipartFile(
                "picture",
                "images.png",
                "png",
                new FileInputStream("D:\\MyProjects\\store\\test-resources\\images.png")
        );

        CrewInfoRequest crewInfoRequest = CrewInfoRequest.builder()
                .name("crew2")
                .introduce("crew2 introduce")
                .picture(image)
                .build();

        // when
        crewAdminService.crewInfoUpdate(crew.getId(), crewInfoRequest);
        Crew findCrew = crewRepository.findById(crew.getId()).get();

        // then
        assertThat(findCrew.getId()).isEqualTo(crew.getId());
        assertThat(findCrew.getName()).isEqualTo("crew2");
        assertThat(findCrew.getIntroduce()).isEqualTo("crew2 introduce");
    }

    @Test
    @DisplayName("크루 프로젝트 목록 조회")
    void projectList() throws Exception {
        // given
        Crew crew = crewRepository.save(Crew.builder().build());
        Crew crew2 = crewRepository.save(Crew.builder().build());

        for (int i = 0; i < 10; i++) {
            crewProjectRepository.save(CrewProject.builder()
                    .crew(crew)
                    .build());

            crewProjectRepository.save(CrewProject.builder()
                    .crew(crew2)
                    .build());
        }

        // when
        List<CrewProjectResponse> crewProjects = crewAdminService.projectList(crew.getId());

        // then
        assertThat(crewProjects.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("크루 프로젝트 추가")
    void addProject() throws Exception {
        // given
        Crew crew = crewRepository.save(Crew.builder().build());
        ProjectRequest projectRequest = ProjectRequest.builder()
                .name("project")
                .introduce("crew's project")
                .link("link")
                .build();

        // when
        crewAdminService.addProject(crew.getId(), projectRequest);
        List<CrewProject> projects = crewProjectRepository.findAll();

        // then
        assertThat(projects.size()).isEqualTo(1);
        assertThat(projects.get(0).getName()).isEqualTo("project");
        assertThat(projects.get(0).getIntroduce()).isEqualTo("crew's project");
        assertThat(projects.get(0).getLink()).isEqualTo("link");
    }

    @Test
    @DisplayName("크루 프로젝트 수정")
    void updateProject() throws Exception {
        // given
        Crew crew = crewRepository.save(Crew.builder().build());

        CrewProject crewProject = crewProjectRepository.save(CrewProject.builder()
                .crew(crew)
                .build());

        ProjectRequest projectRequest = ProjectRequest.builder()
                .name("project")
                .introduce("crew's project")
                .link("link")
                .build();

        // when
        crewAdminService.updateProject(crew.getId(), crewProject.getId(), projectRequest);
        CrewProject findCrewProject = crewProjectRepository.findById(crewProject.getId()).get();

        // then
        assertThat(findCrewProject.getName()).isEqualTo("project");
        assertThat(findCrewProject.getIntroduce()).isEqualTo("crew's project");
        assertThat(findCrewProject.getLink()).isEqualTo("link");
    }

    @Test
    @DisplayName("크루 프로젝트 삭제")
    void removeProject() throws Exception {
        // given
        Crew crew = crewRepository.save(Crew.builder().build());
        CrewProject crewProject = crewProjectRepository.save(CrewProject.builder()
                .crew(crew)
                .build());

        // when
        crewAdminService.removeProject(crew.getId(), crewProject.getId());
        Optional<CrewProject> findCrewProject = crewProjectRepository.findById(crewProject.getId());

        // then
        assertThat(findCrewProject).isEmpty();
    }
}