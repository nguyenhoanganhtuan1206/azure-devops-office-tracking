package com.openwt.officetracking.domain.ability;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class Ability {

    private UUID id;

    private String name;

    private UUID abilityCategoryId;
}