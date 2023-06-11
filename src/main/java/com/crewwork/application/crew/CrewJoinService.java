package com.crewwork.application.crew;

import com.crewwork.domain.crew.Crew;
import com.crewwork.domain.crew.CrewRepository;
import com.crewwork.domain.crew.crewjoin.CrewJoin;
import com.crewwork.domain.crew.crewjoin.CrewJoinRepository;
import com.crewwork.domain.crew.crewmember.CrewMember;
import com.crewwork.domain.crew.crewmember.CrewMemberRepository;
import com.crewwork.domain.crew.crewmember.CrewRole;
import com.crewwork.domain.member.Member;
import com.crewwork.domain.member.MemberRepository;
import com.crewwork.structure.exception.BusinessException;
import com.crewwork.structure.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrewJoinService {

    private final CrewJoinRepository crewJoinRepository;
    private final CrewRepository crewRepository;
    private final MemberRepository memberRepository;
    private final CrewMemberRepository crewMemberRepository;

    public List<CrewJoin> getList(Long crewId) {
        return crewJoinRepository.findAllByCrewId(crewId);
    }

    @Transactional
    public Long request(Long memberId, Long crewId) {
        if(crewJoinRepository.existsByMemberIdAndCrewId(memberId, crewId)) {
            throw new BusinessException(ErrorCode.ALREADY_CREW_JOIN_APPLY);
        }
        if (crewMemberRepository.existsByMemberIdAndCrewId(memberId, crewId)) {
            throw new BusinessException(ErrorCode.ALREADY_CREW_MEMBER);
        }

        Crew crew = crewRepository.findById(crewId).orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        CrewJoin crewJoin = crewJoinRepository.save(CrewJoin.builder()
                .crew(crew)
                .member(member)
                .build());

        return crewJoin.getId();
    }

    @Transactional
    public Long accept(Long crewJoinId) {
        CrewJoin crewJoin = crewJoinRepository.findById(crewJoinId).orElseThrow(() -> new BusinessException(ErrorCode.CREWJOIN_NOT_FOUND));
        Member member = crewJoin.getMember();
        Crew crew = crewJoin.getCrew();

        CrewMember crewMember = crewMemberRepository.save(CrewMember.builder()
                .member(member)
                .crew(crew)
                .role(CrewRole.MEMBER)
                .build());

        crewJoinRepository.delete(crewJoin);

        return crewMember.getId();
    }

    @Transactional
    public void refuse(Long crewJoinId) {
        try {
            crewJoinRepository.deleteById(crewJoinId);
        } catch (EmptyResultDataAccessException e) {
            throw new BusinessException(ErrorCode.CREWJOIN_NOT_FOUND);
        }
    }


}
