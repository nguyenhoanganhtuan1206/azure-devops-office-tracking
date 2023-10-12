package com.openwt.officetracking.domain.course_assignment;

import com.openwt.officetracking.domain.review_status.ReviewStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.UUID;

@Getter
@Setter
@Builder
@With
public class CourseAssignment {

    private UUID id;

    private UUID courseId;

    private UUID coachId;

    private UUID menteeId;

    private UUID mentorId;

    private ReviewStatus mentorReviewStatus;

    private ReviewStatus coachReviewStatus;

    private ReviewStatus reviewStatus;
}
