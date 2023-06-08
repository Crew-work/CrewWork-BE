package com.crewwork.application.crew;

import com.crewwork.domain.crew.Crew;
import com.crewwork.domain.crew.CrewRepository;
import com.crewwork.domain.crew.crewmember.CrewMember;
import com.crewwork.domain.crew.crewmember.CrewMemberRepository;
import com.crewwork.domain.crew.crewmember.CrewRole;
import com.crewwork.domain.member.Member;
import com.crewwork.domain.member.MemberRepository;
import com.crewwork.structure.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CrewControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired CrewRepository crewRepository;
    @Autowired CrewMemberRepository crewMemberRepository;
    @Autowired MemberRepository memberRepository;


    @Test
    @WithMockCustomUser(username = "test user", nickname = "nick")
    void newCrew() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "picture",
                "images.png",
                "png",
                new FileInputStream("D:\\MyProjects\\store\\test-resources\\images.png"));


        mockMvc.perform(multipart(HttpMethod.POST, "/api/crews/new")
                        .file(image)
                        .param("name", "crew")
                        .param("introduce", "crew introduce")
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.crewId").exists())
                .andDo(print());
    }

    @Test
    @WithMockCustomUser(username = "user", nickname = "me")
    @Transactional
    void myCrews() throws Exception {
//        dataInit();
//
//        mockMvc.perform(get("/api/crews/me?page=0&size=5"))
//                .andExpect(jsonPath("$.first").value(true))
//                .andExpect(jsonPath("$.last").value(false))
//                .andExpect(jsonPath("$.totalElements").value(10))
//                .andExpect(jsonPath("$.totalPages").value(2))
//                .andExpect(jsonPath("$.pageNumber").value(0))
//                .andExpect(jsonPath("$.contents", Matchers.hasSize(5)))
//                .andDo(print());
    }

    @Test
    void crews() {
        // given

        // when

        // then
    }

    @Test
    void crewDetails() {
        // given

        // when

        // then
    }

    private void dataInit() {
        Member me = memberRepository.findByUsername("test user")
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .username("test user")
                        .nickname("me")
                        .picture("picture")
                        .introduce("i'm me")
                        .occupation("backend")
                        .contact("member@crew.work")
                        .techStack("Spring,JAVA")
                        .build()));

        for (int i = 1; i <= 10; i++) {
            Crew crew = crewRepository.save(Crew.builder()
                    .name("crew" + i)
                    .introduce("crew" + i + " is nice")
                    .picture("picture")
                    .build());

            crewMemberRepository.save(CrewMember.builder()
                    .crew(crew)
                    .member(me)
                    .role(CrewRole.OWNER)
                    .build());

            for (int j = 0; j < 10; j++) {
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
    }
}