package com.crewwork.domain.project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberProjectRepository extends JpaRepository<MemberProject, Long> {

    List<MemberProject> findAllByMemberId(Long memberId);
}
