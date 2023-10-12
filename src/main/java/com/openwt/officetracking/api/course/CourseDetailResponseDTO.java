package com.openwt.officetracking.api.course;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Builder
public class CourseDetailResponseDTO {

    private UUID id;

    private String name;

    private String description;

    private Instant createdAt;

    private Instant startAt;

    private Instant endAt;

    private Instant updatedAt;

    private List<CourseAssignmentResponseDTO> courseAssignments;
}
