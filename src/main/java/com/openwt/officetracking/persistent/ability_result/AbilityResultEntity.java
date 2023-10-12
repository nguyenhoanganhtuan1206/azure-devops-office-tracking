package com.openwt.officetracking.persistent.ability_result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Table(name = "ability_results")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AbilityResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID abilityId;

    private UUID courseAssignmentId;

    private boolean isMentor;

    private Integer point;

    private Instant createdAt;

    private Instant updatedAt;
}
