package com.openwt.officetracking.domain.course;

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
public class CoachCourse {

    private UUID id;

    private String name;

    private Instant startAt;

    private Instant endAt;

    private CourseStatus status;

    private List<CourseMentee> coachMentees;
}
