package com.openwt.officetracking.domain.course_assignment;

import com.openwt.officetracking.persistent.course_assignment.CourseAssignmentEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class CourseAssignmentMapper {

    public static CourseAssignment toCourseAssignment(final CourseAssignmentEntity courseAssignmentEntity) {
        return CourseAssignment.builder()
                .id(courseAssignmentEntity.getId())
                .coachId(courseAssignmentEntity.getCoachId())
                .mentorId(courseAssignmentEntity.getMentorId())
                .menteeId(courseAssignmentEntity.getMenteeId())
                .courseId(courseAssignmentEntity.getCourseId())
                .mentorReviewStatus(courseAssignmentEntity.getMentorReviewStatus())
                .coachReviewStatus(courseAssignmentEntity.getCoachReviewStatus())
                .reviewStatus(courseAssignmentEntity.getReviewStatus())
                .build();
    }

    public static List<CourseAssignment> toCourseAssignments(final List<CourseAssignmentEntity> courseAssignmentEntities) {
        return courseAssignmentEntities.stream()
                .map(CourseAssignmentMapper::toCourseAssignment)
                .toList();
    }
}
