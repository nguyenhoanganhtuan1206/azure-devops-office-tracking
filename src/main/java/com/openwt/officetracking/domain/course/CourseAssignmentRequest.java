package com.openwt.officetracking.domain.course;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseAssignmentRequest {

    private UUID mentorId;

    private List<CoachAssignmentRequest> coachAssignments;
}
