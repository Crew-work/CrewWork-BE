package com.crewwork.application.crew;

import com.crewwork.domain.crew.Crew;
import com.crewwork.domain.crew.CrewRepository;
import com.crewwork.domain.crew.crewjoin.CrewJoin;
import com.crewwork.domain.crew.crewjoin.CrewJoinRepository;
import com.crewwork.domain.member.Member;
import com.crewwork.domain.member.MemberRepository;
import com.crewwork.structure.exception.BusinessException;
import com.crewwork.structure.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrewJoinService {

    private final CrewJoinRepository crewJoinRepository;
    private final CrewRepository crewRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void apply(Long memberId, Long crewId) {
        if(crewJoinRepository.existsByMemberIdAndCrewId(memberId, crewId)) {
            throw new BusinessException(ErrorCode.ALREADY_CREW_JOIN_APPLY);
        }

        Crew crew = crewRepository.findById(crewId).orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        crewJoinRepository.save(CrewJoin.builder()
                .crew(crew)
                .member(member)
                .build());
    }


}
