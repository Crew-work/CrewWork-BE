package com.crewwork.application.login;

import com.crewwork.application.login.dto.WriteMemberInfoRequest;
import com.crewwork.domain.member.Member;
import com.crewwork.domain.member.MemberRepository;
import com.crewwork.structure.exception.BusinessException;
import com.crewwork.structure.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {

    private final MemberRepository memberRepository;

    @Transactional
    public void writeMemberInfo(Long memberId, WriteMemberInfoRequest writeMemberInfoRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        member.changeInfo(member.getNickname(),
                member.getPicture(),
                writeMemberInfoRequest.getContact(),
                writeMemberInfoRequest.getOccupation(),
                writeMemberInfoRequest.getTechStack(),
                member.getIntroduce());
    }
}
