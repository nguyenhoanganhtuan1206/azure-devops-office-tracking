package com.openwt.officetracking.domain.ability_result;

import com.openwt.officetracking.domain.ability_category.AbilityCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

@Getter
@Setter
@Builder
@With
public class AbilityCategoryAverage {

    private AbilityCategory abilityCategory;

    private double averageScore;
}
