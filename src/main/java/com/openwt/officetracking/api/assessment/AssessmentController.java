package com.openwt.officetracking.api.assessment;

import com.openwt.officetracking.api.ability_result.AbilityResultRequestDTO;
import com.openwt.officetracking.api.ability_result.AbilityResultResponseDTO;
import com.openwt.officetracking.domain.ability.AbilityService;
import com.openwt.officetracking.domain.ability_result.AbilityResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.api.ability_result.AbilityResultRequestDTOMapper.toAbilityResultDetail;
import static com.openwt.officetracking.api.ability_result.AbilityResultResponseDTOMapper.toAbilityResultResponseDTO;
import static com.openwt.officetracking.api.assessment.AbilityDTOMapper.toAbilityCategoryDetailDTOs;
import static com.openwt.officetracking.api.assessment.AbilityDTOMapper.toMentorAbilityCategoryAverageDTOs;

@RestController
@RequestMapping("api/v1/assessments")
@RequiredArgsConstructor
public class AssessmentController {

    private final AbilityResultService abilityResultService;

    private final AbilityService abilityService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("categories")
    public List<AbilityCategoryDetailDTO> findAbilityCategoryDetails() {
        return toAbilityCategoryDetailDTOs(abilityService.findAbilityCategoryDetails());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("results")
    public AbilityResultResponseDTO findDetailAbilityResult(@RequestParam(required = false) final UUID menteeId, @RequestParam final UUID courseId) {
        return toAbilityResultResponseDTO(abilityResultService.findAbilityResultDetailByCourseId(menteeId, courseId));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping
    public AbilityResultResponseDTO saveMentorAssignments(final @RequestBody AbilityResultRequestDTO abilityResultRequestDTO) {
        return toAbilityResultResponseDTO(abilityResultService.saveAbilityResultDetail(toAbilityResultDetail(abilityResultRequestDTO)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("average-score")
    public List<MentorAbilityCategoryAverageDTO> calculateAveragesScoreByMenteeAndCourse(@RequestParam final UUID menteeId, @RequestParam final UUID courseId) {
        return toMentorAbilityCategoryAverageDTOs(abilityResultService.calculateAveragesScoreByMenteeAndCourse(menteeId, courseId));
    }
}
