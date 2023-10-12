package com.openwt.officetracking.domain.ability_result;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
public class AbilityResultDetail {

    private UUID courseId;

    private UUID reviewerId;

    private boolean isSubmit;

    private List<MentorAbilityResult> results;
}
