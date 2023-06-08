package com.crewwork.application.crew;

import com.crewwork.application.common.PageResponse;
import com.crewwork.application.crew.dto.CrewCreateRequest;
import com.crewwork.application.crew.dto.CrewCreateResponse;
import com.crewwork.application.crew.dto.CrewDetailResponse;
import com.crewwork.application.crew.dto.CrewResponse;
import com.crewwork.structure.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/crews")
@RequiredArgsConstructor
public class CrewController {

    private final CrewService crewService;

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public CrewCreateResponse newCrew(CrewCreateRequest crewCreateRequest) throws IOException {
        Long crewId = crewService.newCrew(crewCreateRequest);
        return new CrewCreateResponse(crewId);
    }

    @GetMapping("/me")
    public PageResponse<CrewResponse> myCrews(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                 Pageable pageable) {

        return crewService.getMyCrews(principalDetails.getMember().getId(), pageable);
    }

    @GetMapping
    public PageResponse<CrewResponse> crews(Pageable pageable) {
        return crewService.getCrews(pageable);
    }

    @GetMapping("/{crewId}")
    public CrewDetailResponse crewDetails(@PathVariable("crewId") Long crewId) {
        return crewService.getCrewDetail(crewId);
    }
}
