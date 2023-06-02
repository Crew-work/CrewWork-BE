package com.crewwork.domain.crew.crewrecruitment;

import com.crewwork.domain.BaseEntity;
import com.crewwork.domain.crew.Crew;
import com.crewwork.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrewRecruitmentBoard extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_recruitment_board_Id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id")
    private Crew crew;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder
    private CrewRecruitmentBoard(Crew crew, Member writer, String title, String content) {
        this.crew = crew;
        this.writer = writer;
        this.title = title;
        this.content = content;
    }
}
