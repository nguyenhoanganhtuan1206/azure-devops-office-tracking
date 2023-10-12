package com.openwt.officetracking.api.assessment;

import com.openwt.officetracking.domain.ability_category.AbilityCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

@Getter
@Setter
@Builder
@With
public class AbilityCategoryAverageDTO {

    private AbilityCategory abilityCategory;

    private double averageScore;
}
