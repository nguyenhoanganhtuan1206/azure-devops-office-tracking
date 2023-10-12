package com.openwt.officetracking.api.assessment;

import com.openwt.officetracking.domain.ability.Ability;
import com.openwt.officetracking.domain.ability.AbilityCategoryDetail;
import com.openwt.officetracking.domain.ability_result.AbilityCategoryAverage;
import com.openwt.officetracking.domain.ability_result.MentorAbilityCategoryAverage;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class AbilityDTOMapper {

    public static AbilityDTO toAbilityDTO(final Ability ability) {
        return AbilityDTO.builder()
                .id(ability.getId())
                .abilityCategoryId(ability.getAbilityCategoryId())
                .name(ability.getName())
                .build();
    }

    public static List<AbilityDTO> toAbilityDTOs(final List<Ability> abilities) {
        return abilities.stream()
                .map(AbilityDTOMapper::toAbilityDTO)
                .toList();
    }

    public static AbilityCategoryDetailDTO toAbilityCategoryDetailDTO(final AbilityCategoryDetail abilityCategoryDetail) {
        return AbilityCategoryDetailDTO.builder()
                .id(abilityCategoryDetail.getId())
                .name(abilityCategoryDetail.getName())
                .abilities(toAbilityDTOs(abilityCategoryDetail.getAbilities()))
                .build();
    }

    public static List<AbilityCategoryDetailDTO> toAbilityCategoryDetailDTOs(final List<AbilityCategoryDetail> abilityCategoryDetails) {
        return abilityCategoryDetails.stream()
                .map(AbilityDTOMapper::toAbilityCategoryDetailDTO)
                .toList();
    }

    public static MentorAbilityCategoryAverageDTO toMentorAbilityCategoryAverageDTO(final MentorAbilityCategoryAverage mentorAbilityCategoryAverage) {
        return MentorAbilityCategoryAverageDTO.builder()
                .reviewerId(mentorAbilityCategoryAverage.getReviewerId())
                .isMentor(mentorAbilityCategoryAverage.isMentor())
                .averagesResults(toAbilityCategoryAverageDTOs(mentorAbilityCategoryAverage.getAveragesResults()))
                .build();
    }

    public static List<MentorAbilityCategoryAverageDTO> toMentorAbilityCategoryAverageDTOs(final List<MentorAbilityCategoryAverage> mentorAbilityCategoryAverages) {
        return mentorAbilityCategoryAverages.stream()
                .map(AbilityDTOMapper::toMentorAbilityCategoryAverageDTO)
                .toList();
    }

    public static AbilityCategoryAverageDTO toAbilityCategoryAverageDTO(final AbilityCategoryAverage abilityCategoryAverage) {
        return AbilityCategoryAverageDTO.builder()
                .abilityCategory(abilityCategoryAverage.getAbilityCategory())
                .averageScore(abilityCategoryAverage.getAverageScore())
                .build();
    }

    public static List<AbilityCategoryAverageDTO> toAbilityCategoryAverageDTOs(final List<AbilityCategoryAverage> abilityCategoryAverages) {
        return abilityCategoryAverages.stream()
                .map(AbilityDTOMapper::toAbilityCategoryAverageDTO)
                .toList();
    }
}
