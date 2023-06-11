package com.crewwork.application.crew.admin;

import com.crewwork.application.common.ProjectRequest;
import com.crewwork.application.common.ProjectAddResponse;
import com.crewwork.application.crew.admin.dto.CrewInfoRequest;
import com.crewwork.application.crew.admin.dto.CrewInfoResponse;
import com.crewwork.application.crew.admin.dto.CrewProjectResponse;
import com.crewwork.application.file.FileStore;
import com.crewwork.application.file.UploadFile;
import com.crewwork.domain.crew.Crew;
import com.crewwork.domain.crew.CrewRepository;
import com.crewwork.domain.project.CrewProject;
import com.crewwork.domain.project.CrewProjectRepository;
import com.crewwork.structure.exception.BusinessException;
import com.crewwork.structure.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrewAdminService {

    private final CrewRepository crewRepository;
    private final CrewProjectRepository crewProjectRepository;
    private final FileStore fileStore;

    public CrewInfoResponse crewInfo(Long crewId) {
        Crew crew = crewRepository.findById(crewId).orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));
        return new CrewInfoResponse(crew);
    }

    @Transactional
    public void crewInfoUpdate(Long crewId, CrewInfoRequest crewInfoRequest) throws IOException {
        Crew crew = crewRepository.findById(crewId).orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));

        UploadFile uploadFile = fileStore.storeFile(crewInfoRequest.getPicture());
        crew.changeInfo(crewInfoRequest.getName(), crewInfoRequest.getIntroduce(), uploadFile.getStoreFileName());
    }

    public List<CrewProjectResponse> projectList(Long crewId) {
        List<CrewProject> projects = crewProjectRepository.findAllByCrewId(crewId);

        return projects.stream()
                .map(CrewProjectResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectAddResponse addProject(Long crewId, ProjectRequest projectRequest) {
        Crew crew = crewRepository.findById(crewId).orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));

        CrewProject crewProject = crewProjectRepository.save(CrewProject.builder()
                .crew(crew)
                .name(projectRequest.getName())
                .introduce(projectRequest.getIntroduce())
                .link(projectRequest.getLink())
                .build());

        return new ProjectAddResponse(crewProject.getId());
    }

    @Transactional
    public void updateProject(Long crewId, Long projectId, ProjectRequest projectRequest) {
        CrewProject crewProject = crewProjectRepository.findByCrewIdAndId(crewId, projectId).orElseThrow(() -> new BusinessException(ErrorCode.PROJECT_NOT_FOUND));

        crewProject.changeInfo(projectRequest.getName(), projectRequest.getIntroduce(), projectRequest.getLink());
    }

    @Transactional
    public void removeProject(Long crewId, Long projectId) {
        crewProjectRepository.deleteByCrewIdAndId(crewId, projectId);
    }
}
