package com.crewwork.domain.project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CrewProjectRepository extends JpaRepository<CrewProject, Long> {

    List<CrewProject> findAllByCrewId(Long crewId);

    Optional<CrewProject> findByCrewIdAndId(Long crewId, Long crewProjectId);

    void deleteByCrewIdAndId(Long crewId, Long crewProjectId);
}
