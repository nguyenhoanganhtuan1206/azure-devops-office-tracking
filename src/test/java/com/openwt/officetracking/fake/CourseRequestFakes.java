package com.openwt.officetracking.fake;

import com.openwt.officetracking.domain.course.CourseAssignmentRequest;
import com.openwt.officetracking.domain.course.CourseRequest;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static com.openwt.officetracking.fake.MenteeCoachFakes.buildMenteeCoachRequests;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class CourseRequestFakes {

    public static CourseRequest buildCourseRequest() {
        return CourseRequest.builder()
                .name(randomAlphabetic(9))
                .description(randomAlphabetic(9))
                .startAt(Instant.now())
                .endAt(Instant.now().plus(10, ChronoUnit.MINUTES))
                .courseAssignments(buildCourseAssignmentRequests())
                .build();
    }

    public static CourseAssignmentRequest buildCourseAssignmentRequest() {
        return CourseAssignmentRequest.builder()
                .mentorId(randomUUID())
                .coachAssignments(buildMenteeCoachRequests())
                .build();
    }

    public static List<CourseAssignmentRequest> buildCourseAssignmentRequests() {
        return buildList(CourseRequestFakes::buildCourseAssignmentRequest);
    }
}
