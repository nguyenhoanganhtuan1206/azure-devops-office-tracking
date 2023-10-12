package com.openwt.officetracking.domain.ability_result;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class PointAndAbilityResult {

    private Integer point;

    private UUID abilityId;
}
