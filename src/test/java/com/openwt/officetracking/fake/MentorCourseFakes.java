package com.openwt.officetracking.fake;

import com.openwt.officetracking.domain.course.CourseMentee;
import com.openwt.officetracking.domain.course.MentorCourse;
import com.openwt.officetracking.domain.course_status.CourseStatus;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static com.openwt.officetracking.fake.CommonFakes.randomInstant;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class MentorCourseFakes {

    public static MentorCourse buildMentorCourse() {
        return MentorCourse.builder()
                .id(randomUUID())
                .name(randomAlphabetic(3, 10))
                .startAt(randomInstant())
                .endAt(randomInstant())
                .status(CourseStatus.IN_PROGRESS)
                .courseMentees(randomCourseMenteeInfos())
                .build();
    }

    public static List<MentorCourse> buildMentorCourses() {
        return buildList(MentorCourseFakes::buildMentorCourse);
    }

    private static List<CourseMentee> randomCourseMenteeInfos() {
        return singletonList(CourseMentee.builder()
                .menteeId(randomUUID())
                .firstName(randomAlphabetic(3, 10))
                .lastName(randomAlphabetic(3, 10))
                .build());
    }
}
