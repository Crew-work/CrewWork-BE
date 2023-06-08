package com.crewwork.domain.crew.crewjoin;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CrewJoinRepository extends JpaRepository<CrewJoin, Long> {

    boolean existsByMemberIdAndCrewId(Long memberId, Long crewId);
}
