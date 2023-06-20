package com.crewwork.application.member.dto;

import com.crewwork.domain.member.Member;
import lombok.Getter;

@Getter
public class MemberProfileResponse {

    private String nickname;
    private String picture;
    private String contact;
    private String occupation;
    private String techStack;
    private String introduce;

    public MemberProfileResponse(Member member) {
        this.nickname = member.getNickname();
        this.picture = member.getPicture();
        this.contact = member.getContact();
        this.occupation = member.getOccupation();
        this.techStack = member.getTechStack();
        this.introduce = member.getIntroduce();
    }
}
