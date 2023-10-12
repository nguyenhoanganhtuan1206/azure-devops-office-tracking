package com.openwt.officetracking.persistent.course_assignment;

import com.openwt.officetracking.domain.review_status.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "course_assignments")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CourseAssignmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID courseId;

    private UUID coachId;

    private UUID menteeId;

    private UUID mentorId;

    @Enumerated(EnumType.STRING)
    private ReviewStatus mentorReviewStatus;

    @Enumerated(EnumType.STRING)
    private ReviewStatus coachReviewStatus;

    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;
}
