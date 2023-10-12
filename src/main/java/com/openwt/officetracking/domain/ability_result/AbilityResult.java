package com.openwt.officetracking.domain.ability_result;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
public class AbilityResult {

    private UUID id;

    private UUID abilityId;

    private UUID courseAssignmentId;

    private boolean isMentor;

    private Integer point;

    private Instant createdAt;

    private Instant updatedAt;
}