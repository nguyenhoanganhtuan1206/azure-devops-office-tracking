package com.openwt.officetracking.api.course;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class CourseRequestDTO {

    private String name;

    private String description;

    private Instant startAt;

    private Instant endAt;

    private List<CourseAssignmentRequestDTO> courseAssignments;
}
