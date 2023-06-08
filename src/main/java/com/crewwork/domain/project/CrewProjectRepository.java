package com.crewwork.domain.project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrewProjectRepository extends JpaRepository<CrewProject, Long> {

    List<CrewProject> findAllByCrewId(Long crewId);
}
