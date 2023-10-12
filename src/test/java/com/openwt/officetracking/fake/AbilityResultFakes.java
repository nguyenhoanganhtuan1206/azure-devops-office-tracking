package com.openwt.officetracking.fake;

import com.openwt.officetracking.api.ability_result.AbilityResultRequestDTO;
import com.openwt.officetracking.domain.ability_result.*;
import com.openwt.officetracking.domain.overall_rating.OverallRating;
import com.openwt.officetracking.persistent.ability_result.AbilityResultEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.api.ability_result.AbilityResultResponseDTOMapper.toMentorAbilityResultDTOs;
import static com.openwt.officetracking.fake.AbilityCategoryFakes.buildAbilityCategory;
import static com.openwt.officetracking.fake.CommonFakes.*;
import static java.util.UUID.randomUUID;

@UtilityClass
public class AbilityResultFakes {
    public static AbilityResult buildAbilityResult() {
        return AbilityResult.builder()
                .id(randomUUID())
                .point(randomInteger(5))
                .abilityId(randomUUID())
                .courseAssignmentId(randomUUID())
                .createdAt(randomInstant())
                .build();
    }

    public static AbilityResultEntity buildAbilityResultEntity() {
        return AbilityResultEntity.builder()
                .id(randomUUID())
                .point(randomInteger(5))
                .abilityId(randomUUID())
                .courseAssignmentId(randomUUID())
                .createdAt(randomInstant())
                .build();
    }

    public static AbilityResultDetail buildAbilityResultDetail() {
        return AbilityResultDetail.builder()
                .reviewerId(randomUUID())
                .courseId(randomUUID())
                .results(buildMentorAbilityResults())
                .build();
    }

    public static MentorAbilityResult buildMentorAbilityResult() {
        return MentorAbilityResult.builder()
                .menteeId(randomUUID())
                .points(buildPointAndAbilities())
                .overallRating(OverallRating.EXCELLENT)
                .total(50)
                .build();
    }

    public static List<MentorAbilityResult> buildMentorAbilityResults() {
        return buildList(AbilityResultFakes::buildMentorAbilityResult);
    }

    public static PointAndAbilityResult buildPointAndAbilityResult() {
        return PointAndAbilityResult.builder()
                .abilityId(randomUUID())
                .point(randomInteger(5))
                .build();
    }

    public static List<PointAndAbilityResult> buildPointAndAbilities() {
        return buildList(AbilityResultFakes::buildPointAndAbilityResult);
    }

    public static MentorAbilityCategoryAverage buildMentorAbilityCategoryAverage() {
        return MentorAbilityCategoryAverage.builder()
                .reviewerId(randomUUID())
                .averagesResults(buildAbilityCategoryAverages())
                .build();
    }

    public static List<MentorAbilityCategoryAverage> buildMentorAbilityCategoryAverages() {
        return buildList(AbilityResultFakes::buildMentorAbilityCategoryAverage);
    }

    public static AbilityCategoryAverage buildAbilityCategoryAverage() {
        return AbilityCategoryAverage.builder()
                .abilityCategory(buildAbilityCategory())
                .averageScore(randomInteger(5))
                .build();
    }

    public static List<AbilityCategoryAverage> buildAbilityCategoryAverages() {
        return buildList(AbilityResultFakes::buildAbilityCategoryAverage);
    }

    public static AbilityResultRequestDTO buildAbilityResultRequest() {
        return AbilityResultRequestDTO.builder()
                .courseId(randomUUID())
                .isSubmit(true)
                .results(toMentorAbilityResultDTOs(buildMentorAbilityResults()))
                .build();
    }
}
