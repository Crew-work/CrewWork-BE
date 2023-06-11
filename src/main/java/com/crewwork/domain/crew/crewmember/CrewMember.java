package com.crewwork.domain.crew.crewmember;

import com.crewwork.domain.BaseTimeEntity;
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
public class CrewMember extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id")
    private Crew crew;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private CrewRole role;

    @Builder
    private CrewMember(Crew crew, Member member, CrewRole role) {
        this.crew = crew;
        this.member = member;
        this.role = role;
    }

    public void changeRole(CrewRole role) {
        this.role = role;
    }
}
