package com.crewwork.domain.member;

import com.crewwork.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    // 회원 인증 데이터
    private String username;
    private String password;
    private String picture;
    private String role;
    private String provider;
    private String providerId;

    // 회원 정보 데이터
    private String nickname;
    private String contact;
    private String occupation;
    private String techStack;
    @Column(columnDefinition = "TEXT")
    private String introduce;

    @Builder
    private Member(String username, String password, String picture, String role, String provider, String providerId, String nickname, String contact, String occupation, String techStack, String introduce) {
        this.username = username;
        this.password = password;
        this.picture = picture;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.nickname = nickname;
        this.contact = contact;
        this.occupation = occupation;
        this.techStack = techStack;
        this.introduce = introduce;
    }

    public void changeInfo(String nickname, String picture, String contact, String occupation, String techStack, String introduce) {
        this.nickname = nickname;
        this.picture = picture;
        this.contact = contact;
        this.occupation = occupation;
        this.techStack = techStack;
        this.introduce = introduce;
    }
}
