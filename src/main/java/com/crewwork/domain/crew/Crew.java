package com.crewwork.domain.crew;

import com.crewwork.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Crew extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_id")
    private Long id;

    private String name;
    @Column(columnDefinition = "TEXT")
    private String introduce;
    private String picture;

    @Builder
    private Crew(String name, String introduce, String picture) {
        this.name = name;
        this.introduce = introduce;
        this.picture = picture;
    }
}

