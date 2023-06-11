package com.crewwork.application.crew;

import com.crewwork.domain.crew.crewmember.CrewMember;
import com.crewwork.domain.crew.crewmember.CrewMemberRepository;
import com.crewwork.domain.crew.crewmember.CrewRole;
import com.crewwork.structure.exception.BusinessException;
import com.crewwork.structure.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrewMemberService {

    private final CrewMemberRepository crewMemberRepository;

    public List<CrewMember> getList(Long crewId) {
        return crewMemberRepository.findAllByCrewId(crewId);
    }

    public void kick(Long crewId, Long memberId) {
        CrewMember crewMember = crewMemberRepository.findByMemberIdAndCrewId(memberId, crewId).orElseThrow(() -> new BusinessException(ErrorCode.CREWMEMBER_NOT_FOUND));

        if(crewMember.getRole() == CrewRole.OWNER) {
            throw new BusinessException(ErrorCode.CANNOT_KICK_OWNER);
        }

        crewMemberRepository.delete(crewMember);
    }

    public void delegate(Long crewId, Long memberId) {
        CrewMember crewMember = crewMemberRepository.findByMemberIdAndCrewId(memberId, crewId).orElseThrow(() -> new BusinessException(ErrorCode.CREWMEMBER_NOT_FOUND));
        CrewMember crewOwner = crewMemberRepository.findOwnerByCrewId(crewId).orElseThrow(() -> new BusinessException(ErrorCode.CREWMEMBER_NOT_FOUND));

        crewMember.changeRole(CrewRole.OWNER);
        crewOwner.changeRole(CrewRole.MEMBER);
    }
}
