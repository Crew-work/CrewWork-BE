package com.crewwork.application.crew;

import com.crewwork.application.common.PageResponse;
import com.crewwork.application.crew.dto.*;
import com.crewwork.domain.crew.Crew;
import com.crewwork.domain.crew.CrewRepository;
import com.crewwork.domain.crew.crewjoin.CrewJoin;
import com.crewwork.domain.crew.crewjoin.CrewJoinBoard;
import com.crewwork.domain.crew.crewjoin.CrewJoinBoardRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final CrewJoinBoardRepository crewJoinBoardRepository;

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

    @Transactional
    public JoinBoardCreateResponse createJoinBoard(JoinBoardCreateRequest joinBoardCreateRequest) {
        Crew crew = crewRepository.findById(joinBoardCreateRequest.getCrewId()).orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));
        CrewJoinBoard board = CrewJoinBoard.builder()
                .crew(crew)
                .title(joinBoardCreateRequest.getTitle())
                .content(joinBoardCreateRequest.getContent())
                .build();

        crewJoinBoardRepository.save(board);

        return new JoinBoardCreateResponse(board);
    }

    @Transactional
    public void updateJoinBoard(Long memberId, Long boardId, JoinBoardUpdateRequest joinBoardUpdateRequest) {
        CrewJoinBoard crewJoinBoard = crewJoinBoardRepository.findById(boardId).orElseThrow(() -> new BusinessException(ErrorCode.JOINBOARD_NOT_FOUND));

        if (crewJoinBoard.getCreatedBy().getId() != memberId) {
            throw new BusinessException(ErrorCode.CANNOT_UPDATE_JOINBOARD);
        }

        crewJoinBoard.changeBoard(joinBoardUpdateRequest.getTitle(), joinBoardUpdateRequest.getContent());
    }

    @Transactional
    public void deleteJoinBoard(Long memberId, Long boardId) {
        CrewJoinBoard crewJoinBoard = crewJoinBoardRepository.findById(boardId).orElseThrow(() -> new BusinessException(ErrorCode.JOINBOARD_NOT_FOUND));

        if (crewJoinBoard.getCreatedBy().getId() != memberId) {
            throw new BusinessException(ErrorCode.CANNOT_DELETE_JOINBOARD);
        }

        crewJoinBoardRepository.delete(crewJoinBoard);
    }

    public JoinBoardDetailResponse getJoinBoardDetail(Long boardId) {
        CrewJoinBoard crewJoinBoard = crewJoinBoardRepository.findById(boardId).orElseThrow(() -> new BusinessException(ErrorCode.JOINBOARD_NOT_FOUND));
        return new JoinBoardDetailResponse(crewJoinBoard);
    }

    public PageResponse<JoinBoardsResponse> getJoinBoards(Pageable pageable) {
        Page<JoinBoardsResponse> boards = crewJoinBoardRepository.findAll(pageable).map(JoinBoardsResponse::new);
        return new PageResponse<>(boards);
    }

}
