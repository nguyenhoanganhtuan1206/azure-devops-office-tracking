package com.openwt.officetracking.api.assessment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class AbilityDTO {

    private UUID id;

    private String name;

    private UUID abilityCategoryId;
}
