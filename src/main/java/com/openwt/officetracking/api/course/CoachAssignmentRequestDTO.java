package com.openwt.officetracking.api.course;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CoachAssignmentRequestDTO {

    private UUID menteeId;

    private UUID coachId;
}
