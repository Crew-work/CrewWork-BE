package com.crewwork.application.member;

import com.crewwork.application.file.FileStore;
import com.crewwork.application.file.UploadFile;
import com.crewwork.application.member.dto.*;
import com.crewwork.domain.member.Member;
import com.crewwork.domain.member.MemberRepository;
import com.crewwork.domain.project.MemberProject;
import com.crewwork.domain.project.MemberProjectRepository;
import com.crewwork.structure.exception.BusinessException;
import com.crewwork.structure.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberProjectRepository memberProjectRepository;
    private final FileStore fileStore;

    public MemberProfileResponse getProfile(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        return new MemberProfileResponse(member);
    }

    @Transactional
    public void updateProfile(Long memberId, UpdateMemberProfileRequest updateMemberProfileRequest) throws IOException {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        fileStore.removeFile(member.getPicture());
        UploadFile uploadFile = fileStore.storeFile(updateMemberProfileRequest.getPicture());


        member.changeInfo(updateMemberProfileRequest.getNickname(),
                uploadFile.getStoreFileName(),
                updateMemberProfileRequest.getContact(),
                updateMemberProfileRequest.getOccupation(),
                updateMemberProfileRequest.getTechStack(),
                updateMemberProfileRequest.getIntroduce());
    }

    public List<MemberProjectsResponse> projectList(Long memberId) {
        List<MemberProject> projects = memberProjectRepository.findAllByMemberId(memberId);

        return projects.stream()
                .map(MemberProjectsResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public AddMemberProjectResponse addProject(Long memberId, AddMemberProjectRequest addMemberProjectRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        MemberProject memberProject = memberProjectRepository.save(MemberProject.builder()
                .member(member)
                .name(addMemberProjectRequest.getName())
                .introduce(addMemberProjectRequest.getIntroduce())
                .link(addMemberProjectRequest.getLink())
                .build());

        return new AddMemberProjectResponse(memberProject);
    }

    @Transactional
    public void updateProject(Long memberId, Long projectId, UpdateMemberProjectRequest updateMemberProjectRequest) {
        MemberProject memberProject = memberProjectRepository.findById(projectId).orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        if (memberProject.getMember().getId() != memberId) {
            throw new BusinessException(ErrorCode.CANNOT_UPDATE_PROJECT);
        }

        memberProject.changeInfo(updateMemberProjectRequest.getName(), updateMemberProjectRequest.getIntroduce(), updateMemberProjectRequest.getLink());
    }

    @Transactional
    public void deleteProject(Long memberId, Long projectId) {
        MemberProject memberProject = memberProjectRepository.findById(projectId).orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        if (memberProject.getMember().getId() != memberId) {
            throw new BusinessException(ErrorCode.CANNOT_UPDATE_PROJECT);
        }

        memberProjectRepository.delete(memberProject);
    }

}
