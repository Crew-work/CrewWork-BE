package com.crewwork.domain.crew.crewmember;

import com.crewwork.domain.crew.Crew;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CrewMemberRepository extends JpaRepository<CrewMember, Long> {

    List<CrewMember> findAllByCrewId(Long crewId);

    @Query("select c from CrewMember cm left join cm.crew c where cm.member.id = :memberId")
    Page<Crew> findCrewByMemberId(@Param("memberId") Long memberId, Pageable pageable);

}
