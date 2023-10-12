package com.openwt.officetracking.domain.course;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@With
@Builder
public class CourseRequest {

    private String name;

    private String description;

    private Instant startAt;

    private Instant endAt;

    private List<CourseAssignmentRequest> courseAssignments;
}
