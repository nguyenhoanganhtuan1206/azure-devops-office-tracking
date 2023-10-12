package com.openwt.officetracking.domain.ability_result;

import com.openwt.officetracking.domain.overall_rating.OverallRating;
import com.openwt.officetracking.domain.review_status.ReviewStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class MentorAbilityResult {

    private UUID menteeId;

    private Integer total;

    private OverallRating overallRating;

    private ReviewStatus mentorReviewStatus;

    private ReviewStatus coachReviewStatus;

    private ReviewStatus reviewStatus;

    private boolean isMentor;

    private List<PointAndAbilityResult> points;
}
