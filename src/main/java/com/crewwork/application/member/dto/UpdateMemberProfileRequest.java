package com.crewwork.application.member.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class UpdateMemberProfileRequest {

    private String nickname;
    private MultipartFile picture;
    private String contact;
    private String occupation;
    private String techStack;
    private String introduce;

}
