package com.openwt.officetracking.fake;

import com.openwt.officetracking.domain.course.Course;
import com.openwt.officetracking.domain.course_assign.CourseAssign;
import com.openwt.officetracking.domain.course_status.CourseStatus;
import com.openwt.officetracking.persistent.course.CourseEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static com.openwt.officetracking.fake.CommonFakes.randomInstant;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class CourseFakes {

    public static Course buildCourse() {
        return Course.builder()
                .id(randomUUID())
                .name(randomAlphabetic(3, 10))
                .startAt(randomInstant())
                .endAt(randomInstant())
                .createdAt(randomInstant())
                .updatedAt(randomInstant())
                .status(CourseStatus.IN_PROGRESS)
                .assign(CourseAssign.DONE)
                .description(randomAlphabetic(3, 10))
                .build();
    }

    public static List<Course> buildCourses() {
        return buildList(CourseFakes::buildCourse);
    }

    public static CourseEntity buildCourseEntity() {
        return CourseEntity.builder()
                .id(randomUUID())
                .name(randomAlphabetic(3, 10))
                .startAt(randomInstant())
                .endAt(randomInstant())
                .createdAt(randomInstant())
                .updatedAt(randomInstant())
                .description(randomAlphabetic(3, 10))
                .build();
    }

    public static List<CourseEntity> buildCourseEntities() {
        return buildList(CourseFakes::buildCourseEntity);
    }
}
