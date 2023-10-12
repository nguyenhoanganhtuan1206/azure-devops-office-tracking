package com.openwt.officetracking.api.course;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CourseAssignmentRequestDTO {

    private UUID mentorId;

    private List<CoachAssignmentRequestDTO> coachAssignments;
}
