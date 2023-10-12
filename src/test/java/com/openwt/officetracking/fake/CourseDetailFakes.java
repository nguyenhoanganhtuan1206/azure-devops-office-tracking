package com.openwt.officetracking.fake;

import com.openwt.officetracking.domain.course.CourseAssignmentResponse;
import com.openwt.officetracking.domain.course.CourseDetailResponse;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static com.openwt.officetracking.fake.CommonFakes.randomInstant;
import static com.openwt.officetracking.fake.MenteeCoachFakes.buildMenteeCoachResponses;
import static com.openwt.officetracking.fake.UserFakes.buildUser;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class CourseDetailFakes {

    public static CourseDetailResponse buildCourseDetailResponse() {
        return CourseDetailResponse.builder()
                .id(randomUUID())
                .name(randomAlphabetic(9))
                .startAt(randomInstant())
                .endAt(randomInstant())
                .updatedAt(randomInstant())
                .createdAt(randomInstant())
                .description(randomAlphabetic(9))
                .courseAssignments(buildCourseAssignmentResponses())
                .build();
    }

    public static CourseAssignmentResponse buildCourseAssignmentResponse() {
        return CourseAssignmentResponse.builder()
                .mentor(buildUser())
                .coachAssignments(buildMenteeCoachResponses())
                .build();
    }

    public static List<CourseAssignmentResponse> buildCourseAssignmentResponses() {
        return buildList(CourseDetailFakes::buildCourseAssignmentResponse);
    }
}
