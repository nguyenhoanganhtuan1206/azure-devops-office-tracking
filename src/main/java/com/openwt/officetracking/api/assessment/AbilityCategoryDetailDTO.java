package com.openwt.officetracking.api.assessment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AbilityCategoryDetailDTO {

    private UUID id;

    private String name;

    private List<AbilityDTO> abilities;
}
