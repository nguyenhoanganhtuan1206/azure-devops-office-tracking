package com.openwt.officetracking.domain.ability_category;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class AbilityCategory {

    private UUID id;

    private String name;
}
