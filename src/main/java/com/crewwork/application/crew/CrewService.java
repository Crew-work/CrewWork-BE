package com.crewwork.application.crew;

import com.crewwork.application.common.PageResponse;
import com.crewwork.application.crew.dto.*;
import com.crewwork.application.file.FileStore;
import com.crewwork.application.file.UploadFile;
import com.crewwork.domain.crew.Crew;
import com.crewwork.domain.crew.CrewRepository;
import com.crewwork.domain.crew.crewmember.CrewMember;
import com.crewwork.domain.crew.crewmember.CrewMemberRepository;
import com.crewwork.domain.file.FileRepository;
import com.crewwork.domain.project.CrewProject;
import com.crewwork.domain.project.CrewProjectRepository;
import com.crewwork.structure.exception.BusinessException;
import com.crewwork.structure.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrewService {

    private final CrewRepository crewRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final CrewProjectRepository crewProjectRepository;
    private final FileRepository fileRepository;
    private final FileStore fileStore;

    @Transactional
    public Long newCrew(CrewCreateRequest crewCreateRequest) throws IOException {

        UploadFile uploadFile = fileStore.storeFile(crewCreateRequest.getPicture());
/*
        fileRepository.save(File.builder()
                .originName(uploadFile.getOriginalFileName())
                .storedName(uploadFile.getStoreFileName())
                .fileSize(uploadFile.getFileSize())
                .build());
*/

        Crew crew = crewRepository.save(Crew.builder()
                .name(crewCreateRequest.getName())
                .introduce(crewCreateRequest.getIntroduce())
                .picture(uploadFile.getStoreFileName())
                .build());

        return crew.getId();
    }

    public CrewDetailResponse getCrewDetail(Long crewId) {
        Crew crew = crewRepository.findById(crewId).orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));
        List<CrewMember> crewMembers = crewMemberRepository.findAllByCrewId(crewId);
        List<CrewProject> crewProjects = crewProjectRepository.findAllByCrewId(crewId);
        return new CrewDetailResponse(crew, crewMembers, crewProjects);
    }

    public PageResponse<CrewResponse> getCrews(Pageable pageable) {
        Page<CrewResponse> crewPage = crewRepository.findAll(pageable).map(CrewResponse::new);

        return PageResponse.<CrewResponse>builder()
                .first(crewPage.isFirst())
                .last(crewPage.isLast())
                .totalElements(crewPage.getTotalElements())
                .totalPages(crewPage.getTotalPages())
                .pageNumber(crewPage.getNumber())
                .contents(crewPage.getContent()).build();
    }

    public PageResponse<CrewResponse> getMyCrews(Long memberId, Pageable pageable) {
        Page<Crew> crewPage = crewMemberRepository.findCrewByMemberId(memberId, pageable);
        Page<CrewResponse> result = crewPage.map(CrewResponse::new);

        return PageResponse.<CrewResponse>builder()
                .first(result.isFirst())
                .last(result.isLast())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .pageNumber(result.getNumber())
                .contents(result.getContent()).build();
    }

    @Transactional
    public void leave(Long memberId, Long crewId) {
        crewMemberRepository.deleteByMemberIdAndCrewId(memberId, crewId);
    }

}
