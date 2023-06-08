package com.crewwork.application.crew.dto;

import com.crewwork.domain.crew.Crew;
import com.crewwork.domain.crew.crewmember.CrewMember;
import com.crewwork.domain.crew.crewmember.CrewRole;
import com.crewwork.domain.project.CrewProject;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CrewDetailResponse {
    private final String name;
    private final String introduce;
    private final String picture;
    private final List<MemberDto> members;
    private final List<ProjectDto> projects;

    public CrewDetailResponse(Crew crew, List<CrewMember> crewMembers, List<CrewProject> projects) {
        this.name = crew.getName();
        this.introduce = crew.getIntroduce();
        this.picture = crew.getPicture();
        this.members = crewMembers.stream()
                .map(crewMember -> new MemberDto(crewMember, crewMember.getRole()))
                .collect(Collectors.toList());
        this.projects = projects.stream()
                .map(ProjectDto::new)
                .collect(Collectors.toList());
    }

    @Getter
    private static class MemberDto {
        private final long id;
        private final CrewRole role;
        private final String name;
        private final String occupation;
        private final String picture;

        public MemberDto(CrewMember crewMember, CrewRole role) {
            this.id = crewMember.getMember().getId();
            this.role = role;
            this.name = crewMember.getMember().getNickname();
            this.occupation = crewMember.getMember().getOccupation();
            this.picture = crewMember.getMember().getPicture();
        }
    }

    @Getter
    private static class ProjectDto {
        private final long id;
        private final String name;
        private final String introduce;
        private final String link;

        public ProjectDto(CrewProject crewProject) {
            this.id = crewProject.getId();
            this.name = crewProject.getName();
            this.introduce = crewProject.getIntroduce();
            this.link = crewProject.getLink();
        }
    }
}
