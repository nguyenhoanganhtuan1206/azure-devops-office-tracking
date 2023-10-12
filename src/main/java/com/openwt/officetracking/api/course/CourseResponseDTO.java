package com.openwt.officetracking.api.course;

import com.openwt.officetracking.domain.course_assign.CourseAssign;
import com.openwt.officetracking.domain.course_status.CourseStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@With
public class CourseResponseDTO {

    private UUID id;

    private String name;

    private Instant startAt;

    private Instant endAt;

    private CourseStatus status;

    private CourseAssign assign;
}
