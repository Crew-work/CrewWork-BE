package com.crewwork.domain.project;

import com.crewwork.domain.BaseTimeEntity;
import com.crewwork.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProject extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    private String name;
    @Column(columnDefinition = "TEXT")
    private String introduce;
    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    private MemberProject(String name, String introduce, String link, Member member) {
        this.name = name;
        this.introduce = introduce;
        this.link = link;
        this.member = member;
    }
}
