package com.openwt.officetracking.domain.course_assignment;

import com.openwt.officetracking.persistent.course_assignment.CourseAssignmentStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.domain.course_assignment.CourseAssignmentError.supplyCourseAssignmentNotFound;
import static com.openwt.officetracking.error.CommonError.supplyNotFoundError;

@Service
@RequiredArgsConstructor
public class CourseAssignmentService {

    private final CourseAssignmentStore courseAssignmentStore;

    public CourseAssignment findById(final UUID id) {
        return courseAssignmentStore.findById(id)
                .orElseThrow(supplyCourseAssignmentNotFound("course assignment id", id));
    }

    public CourseAssignment create(final CourseAssignment courseAssignment) {
        return courseAssignmentStore.save(courseAssignment);
    }

    public CourseAssignment findByMentorMenteeAndCourse(final UUID mentorId, final UUID menteeId, final UUID courseId) {
        return courseAssignmentStore.findByMentorMenteeAndCourse(mentorId, menteeId, courseId)
                .orElseThrow(supplyNotFoundError("Course assignment not existing"));
    }

    public List<CourseAssignment> findByCoachMenteeAndCourse(final UUID coachId, final UUID menteeId, final UUID courseId) {
        return courseAssignmentStore.findByCoachMenteeAndCourse(coachId, menteeId, courseId);
    }

    public CourseAssignment findByMentorMenteeCoachAndCourse(final UUID mentorId, final UUID menteeId, final UUID coachId, final UUID courseId) {
        return courseAssignmentStore.findByMentorMenteeCoachAndCourse(mentorId, menteeId, coachId, courseId)
                .orElseThrow(supplyNotFoundError("Course assignment not existing"));
    }

    public List<CourseAssignment> findByCoachId(final UUID coachId) {
        return courseAssignmentStore.findByCoachId(coachId);
    }
    public List<CourseAssignment> findByMenteeId(final UUID menteeId) {
        return courseAssignmentStore.findByMenteeId(menteeId);
    }

    public List<CourseAssignment> findByMentorId(final UUID mentorId) {
        return courseAssignmentStore.findByMentorId(mentorId);
    }

    public List<CourseAssignment> findByMenteeIdAndCourseId(final UUID menteeId, final UUID courseId) {
        return courseAssignmentStore.findByMenteeIdAndCourseId(menteeId, courseId);
    }

    public List<CourseAssignment> findByMentorIdAndCourseId(final UUID mentorId, final UUID courseId) {
        return courseAssignmentStore.findByMentorIdAndCourseId(mentorId, courseId);
    }

    public List<CourseAssignment> findByCoachIdAndCourseId(final UUID coachId, final UUID courseId) {
        return courseAssignmentStore.findByCoachIdAndCourseId(coachId, courseId);
    }

    public CourseAssignment findByCourseIdAndMentorIdAndMenteeId(final UUID courseId, final UUID mentorId, final UUID menteeId) {
        return courseAssignmentStore.findByCourseIdAndMentorIdAndMenteeId(courseId, mentorId, menteeId);
    }

    public List<CourseAssignment> findByCourseId(final UUID courseId) {
        return courseAssignmentStore.findByCourseId(courseId);
    }

    public List<CourseAssignment> findByCourseIdAndMentorIdIsNullAndMenteeIdIsNotNull(final UUID courseId) {
        return courseAssignmentStore.findByCourseIdAndMentorIdIsNullAndMenteeIdIsNotNull(courseId);
    }

    public void deleteUnassignedCourseByMentorId(final UUID mentorId) {
        courseAssignmentStore.deleteUnassignedCourseByMentorId(mentorId);
    }

    public void deleteByCourseId(final UUID courseId) {
        courseAssignmentStore.deleteByCourseId(courseId);
    }

    public void deleteByMentorIdAndCourseId(final UUID mentorId, final UUID courseId) {
        courseAssignmentStore.deleteByMentorIdAndCourseId(mentorId, courseId);
    }

    public void deleteByMenteeIdAndMentorIdAndCourseId(final UUID menteeId, final UUID mentorId, final UUID courseId) {
        courseAssignmentStore.deleteByMenteeIdAndMentorIdAndCourseId(menteeId, mentorId, courseId);
    }

    public void deleteByCourseIdAndMenteeIdWithoutMentorId(final UUID courseId, final UUID menteeId) {
        courseAssignmentStore.deleteByCourseIdAndMenteeIdWithoutMentorId(courseId, menteeId);
    }

    public void deleteByMenteeIdAndCourseId(final UUID menteeId, final UUID courseId) {
        courseAssignmentStore.deleteByMenteeIdAndCourseId(menteeId, courseId);
    }
}
