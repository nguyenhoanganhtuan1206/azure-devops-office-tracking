package com.openwt.officetracking.api.ability_result;

import com.openwt.officetracking.domain.ability_result.AbilityResultDetail;
import com.openwt.officetracking.domain.ability_result.MentorAbilityResult;
import com.openwt.officetracking.domain.ability_result.PointAndAbilityResult;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class AbilityResultResponseDTOMapper {

    public static AbilityResultResponseDTO toAbilityResultResponseDTO(final AbilityResultDetail abilityResultDetail) {
        return AbilityResultResponseDTO.builder()
                .courseId(abilityResultDetail.getCourseId())
                .reviewerId(abilityResultDetail.getReviewerId())
                .results(toMentorAbilityResultDTOs(abilityResultDetail.getResults()))
                .build();
    }

    public static MentorAbilityResultDTO toMentorAbilityResultDTO(final MentorAbilityResult mentorAbilityResult) {
        return MentorAbilityResultDTO.builder()
                .menteeId(mentorAbilityResult.getMenteeId())
                .total(mentorAbilityResult.getTotal())
                .overallRating(mentorAbilityResult.getOverallRating())
                .isMentor(mentorAbilityResult.isMentor())
                .points(toPointAndAbilityResultDTOs(mentorAbilityResult.getPoints()))
                .mentorReviewStatus(mentorAbilityResult.getMentorReviewStatus())
                .coachReviewStatus(mentorAbilityResult.getCoachReviewStatus())
                .reviewStatus(mentorAbilityResult.getReviewStatus())
                .build();
    }

    public static List<MentorAbilityResultDTO> toMentorAbilityResultDTOs(final List<MentorAbilityResult> mentorAbilityResult) {
        return mentorAbilityResult.stream()
                .map(AbilityResultResponseDTOMapper::toMentorAbilityResultDTO)
                .toList();
    }

    public static PointAndAbilityResultDTO toPointAndAbilityResultDTO(final PointAndAbilityResult pointAndAbilityResult) {
        return PointAndAbilityResultDTO.builder()
                .abilityId(pointAndAbilityResult.getAbilityId())
                .point(pointAndAbilityResult.getPoint())
                .build();
    }

    public static List<PointAndAbilityResultDTO> toPointAndAbilityResultDTOs(final List<PointAndAbilityResult> pointAndAbilityResults) {
        return pointAndAbilityResults.stream()
                .map(AbilityResultResponseDTOMapper::toPointAndAbilityResultDTO)
                .toList();
    }
}
