package com.openwt.officetracking.fake;

import com.openwt.officetracking.domain.course.CourseMentor;
import com.openwt.officetracking.domain.course.MenteeCourse;
import com.openwt.officetracking.domain.course_status.CourseStatus;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static com.openwt.officetracking.fake.CommonFakes.randomInstant;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class MenteeCourseFakes {

    public static MenteeCourse buildMenteeCourse() {
        return MenteeCourse.builder()
                .id(randomUUID())
                .name(randomAlphabetic(3, 10))
                .startAt(randomInstant())
                .endAt(randomInstant())
                .status(CourseStatus.IN_PROGRESS)
                .mentors(randomCourseMentorInfos())
                .build();
    }

    public static List<MenteeCourse> buildMenteeCourses() {
        return buildList(MenteeCourseFakes::buildMenteeCourse);
    }

    private static List<CourseMentor> randomCourseMentorInfos() {
        return singletonList(CourseMentor.builder()
        .mentorId(randomUUID())
        .firstName(randomAlphabetic(3, 10))
        .lastName(randomAlphabetic(3, 10))
        .build());
    }
}