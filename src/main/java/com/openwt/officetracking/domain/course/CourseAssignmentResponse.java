package com.openwt.officetracking.domain.course;

import com.openwt.officetracking.domain.user.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseAssignmentResponse {

    private User mentor;

    private List<CoachAssignmentResponse> coachAssignments;
}
