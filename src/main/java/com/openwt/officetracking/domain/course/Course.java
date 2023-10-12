package com.openwt.officetracking.domain.course;

import com.openwt.officetracking.domain.course_assign.CourseAssign;
import com.openwt.officetracking.domain.course_status.CourseStatus;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    private UUID id;

    private String name;

    private String description;

    private Instant startAt;

    private Instant endAt;

    private CourseStatus status;

    private CourseAssign assign;

    private Instant createdAt;

    private Instant updatedAt;
}
