package com.openwt.officetracking.api.mentorship;

import com.openwt.officetracking.domain.course.CourseMentor;
import com.openwt.officetracking.domain.course_status.CourseStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@With
public class MenteeCourseResponseDTO {

    private UUID id;

    private String name;

    private Instant startAt;

    private Instant endAt;

    private CourseStatus status;

    private List<CourseMentor> mentors;
}