package com.openwt.officetracking.api.ability_result;

import com.openwt.officetracking.domain.review_status.ReviewStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AbilityResultDetailDTO {

    private ReviewStatus reviewStatus;

    private UUID mentorId;

    private List<MentorAbilityResultDTO> results;
}