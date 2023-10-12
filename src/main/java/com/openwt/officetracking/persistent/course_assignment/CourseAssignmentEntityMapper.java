package com.openwt.officetracking.persistent.course_assignment;

import com.openwt.officetracking.domain.course_assignment.CourseAssignment;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CourseAssignmentEntityMapper {

    public static CourseAssignment toCourseAssignment(final CourseAssignmentEntity courseAssignmentEntity) {
        return CourseAssignment.builder()
                .id(courseAssignmentEntity.getId())
                .coachId(courseAssignmentEntity.getCoachId())
                .courseId(courseAssignmentEntity.getCourseId())
                .mentorId(courseAssignmentEntity.getMentorId())
                .menteeId(courseAssignmentEntity.getMenteeId())
                .mentorReviewStatus(courseAssignmentEntity.getMentorReviewStatus())
                .coachReviewStatus(courseAssignmentEntity.getCoachReviewStatus())
                .reviewStatus(courseAssignmentEntity.getReviewStatus())
                .build();
    }

    public static CourseAssignmentEntity toCourseAssignmentEntity(final CourseAssignment courseAssignment) {
        return CourseAssignmentEntity.builder()
                .id(courseAssignment.getId())
                .coachId(courseAssignment.getCoachId())
                .courseId(courseAssignment.getCourseId())
                .mentorId(courseAssignment.getMentorId())
                .menteeId(courseAssignment.getMenteeId())
                .mentorReviewStatus(courseAssignment.getMentorReviewStatus())
                .coachReviewStatus(courseAssignment.getCoachReviewStatus())
                .reviewStatus(courseAssignment.getReviewStatus())
                .build();
    }
}
