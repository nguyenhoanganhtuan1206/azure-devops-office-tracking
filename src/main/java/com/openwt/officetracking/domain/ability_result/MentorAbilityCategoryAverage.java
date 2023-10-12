package com.openwt.officetracking.domain.ability_result;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@With
public class MentorAbilityCategoryAverage {

    private UUID reviewerId;

    private boolean isMentor;

    private List<AbilityCategoryAverage> averagesResults;
}
