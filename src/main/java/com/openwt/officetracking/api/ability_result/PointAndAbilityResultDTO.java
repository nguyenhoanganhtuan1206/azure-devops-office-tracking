package com.openwt.officetracking.api.ability_result;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class PointAndAbilityResultDTO {

    private Integer point;

    private UUID abilityId;
}
