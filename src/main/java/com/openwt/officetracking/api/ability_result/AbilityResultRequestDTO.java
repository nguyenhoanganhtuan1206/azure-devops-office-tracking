package com.openwt.officetracking.api.ability_result;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AbilityResultRequestDTO {

    private UUID courseId;

    private boolean isSubmit;

    private List<MentorAbilityResultDTO> results;
}