package com.openwt.officetracking.fake;

import com.openwt.officetracking.domain.course_assignment.CourseAssignment;
import com.openwt.officetracking.domain.review_status.ReviewStatus;
import com.openwt.officetracking.persistent.course_assignment.CourseAssignmentEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static java.util.UUID.randomUUID;

@UtilityClass
public class CourseAssignmentFakes {

    public static CourseAssignment buildCourseAssignment() {
        return CourseAssignment.builder()
                .id(randomUUID())
                .coachId(randomUUID())
                .courseId(randomUUID())
                .mentorId(randomUUID())
                .menteeId(randomUUID())
                .reviewStatus(ReviewStatus.SUBMITTED)
                .build();
    }

    public static List<CourseAssignment> buildCourseAssignments() {
        return buildList(CourseAssignmentFakes::buildCourseAssignment);
    }

    public static CourseAssignmentEntity buildCourseAssignmentEntity() {
        return CourseAssignmentEntity.builder()
                .id(randomUUID())
                .coachId(randomUUID())
                .courseId(randomUUID())
                .mentorId(randomUUID())
                .menteeId(randomUUID())
                .build();
    }

    public static List<CourseAssignmentEntity> buildCourseAssignmentEntities() {
        return buildList(CourseAssignmentFakes::buildCourseAssignmentEntity);
    }
}
