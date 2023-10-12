package com.openwt.officetracking.domain.course;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@With
@Builder
public class CourseDetailResponse {

    private UUID id;

    private String name;

    private String description;

    private Instant startAt;

    private Instant endAt;

    private Instant updatedAt;

    private Instant createdAt;

    private List<CourseAssignmentResponse> courseAssignments;
}
