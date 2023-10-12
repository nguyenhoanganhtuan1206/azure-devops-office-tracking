package com.openwt.officetracking.api.ability_result;

import com.openwt.officetracking.domain.ability_result.AbilityResultDetail;
import com.openwt.officetracking.domain.ability_result.MentorAbilityResult;
import com.openwt.officetracking.domain.ability_result.PointAndAbilityResult;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class AbilityResultRequestDTOMapper {

    public static AbilityResultDetail toAbilityResultDetail(final AbilityResultRequestDTO abilityResultRequestDTO) {
        return AbilityResultDetail.builder()
                .courseId(abilityResultRequestDTO.getCourseId())
                .isSubmit(abilityResultRequestDTO.isSubmit())
                .results(toMentorAbilityResults(abilityResultRequestDTO.getResults()))
                .build();
    }

    public static MentorAbilityResult toMentorAbilityResult(final MentorAbilityResultDTO mentorAbilityResultDTO) {
        return MentorAbilityResult.builder()
                .menteeId(mentorAbilityResultDTO.getMenteeId())
                .isMentor(mentorAbilityResultDTO.isMentor())
                .points(toPointAndAbilityResults(mentorAbilityResultDTO.getPoints()))
                .build();
    }

    public static List<MentorAbilityResult> toMentorAbilityResults(final List<MentorAbilityResultDTO> mentorAbilityResultDTOs) {
        return mentorAbilityResultDTOs.stream()
                .map(AbilityResultRequestDTOMapper::toMentorAbilityResult)
                .toList();
    }

    public static PointAndAbilityResult toPointAndAbilityResult(final PointAndAbilityResultDTO pointAndAbilityResultDTO) {
        return PointAndAbilityResult.builder()
                .abilityId(pointAndAbilityResultDTO.getAbilityId())
                .point(pointAndAbilityResultDTO.getPoint())
                .build();
    }

    public static List<PointAndAbilityResult> toPointAndAbilityResults(final List<PointAndAbilityResultDTO> pointAndAbilityResultDTOs) {
        return pointAndAbilityResultDTOs.stream()
                .map(AbilityResultRequestDTOMapper::toPointAndAbilityResult)
                .toList();
    }
}
