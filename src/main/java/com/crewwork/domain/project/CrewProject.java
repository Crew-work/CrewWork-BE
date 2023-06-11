package com.crewwork.domain.project;

import com.crewwork.domain.BaseEntity;
import com.crewwork.domain.crew.Crew;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrewProject extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_project_id")
    private Long id;

    private String name;
    @Column(columnDefinition = "TEXT")
    private String introduce;
    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id")
    private Crew crew;
    @Builder
    private CrewProject(String name, String introduce, String link, Crew crew) {
        this.name = name;
        this.introduce = introduce;
        this.link = link;
        this.crew = crew;
    }

    public void changeInfo(String name, String introduce, String link) {
        this.name = name;
        this.introduce = introduce;
        this.link = link;
    }
}
