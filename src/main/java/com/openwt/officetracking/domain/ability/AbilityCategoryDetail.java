package com.openwt.officetracking.domain.ability;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AbilityCategoryDetail {

    private UUID id;

    private String name;

    private List<Ability> abilities;
}
