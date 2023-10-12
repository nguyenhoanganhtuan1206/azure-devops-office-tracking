package com.openwt.officetracking.persistent.course_assignment;

import com.openwt.officetracking.domain.course_assignment.CourseAssignment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.domain.course_assignment.CourseAssignmentMapper.toCourseAssignments;
import static com.openwt.officetracking.persistent.course_assignment.CourseAssignmentEntityMapper.toCourseAssignment;
import static com.openwt.officetracking.persistent.course_assignment.CourseAssignmentEntityMapper.toCourseAssignmentEntity;

@Repository
@RequiredArgsConstructor
public class CourseAssignmentStore {

    private final CourseAssignmentRepository courseAssignmentRepository;

    public Optional<CourseAssignment> findById(final UUID id) {
        return courseAssignmentRepository.findById(id)
                .map(CourseAssignmentEntityMapper::toCourseAssignment);
    }

    public CourseAssignment save(final CourseAssignment courseAssignment) {
        return toCourseAssignment(courseAssignmentRepository.save(toCourseAssignmentEntity(courseAssignment)));
    }

    public Optional<CourseAssignment> findByMentorMenteeAndCourse(final UUID mentorId, final UUID menteeId, final UUID courseId) {
        return courseAssignmentRepository.findByMentorIdAndMenteeIdAndCourseId(mentorId, menteeId, courseId)
                .map(CourseAssignmentEntityMapper::toCourseAssignment);
    }

    public Optional<CourseAssignment> findByMentorMenteeCoachAndCourse(final UUID mentorId, final UUID menteeId, final UUID coachId, final UUID courseId) {
        return courseAssignmentRepository.findByMentorIdAndMenteeIdAndCoachIdAndCourseId(mentorId, menteeId, coachId, courseId)
                .map(CourseAssignmentEntityMapper::toCourseAssignment);
    }

    public List<CourseAssignment> findByCoachMenteeAndCourse(final UUID coachId, final UUID menteeId, final UUID courseId) {
        return toCourseAssignments(courseAssignmentRepository.findByCoachIdAndMenteeIdAndCourseId(coachId, menteeId, courseId));
    }

    public List<CourseAssignment> findByMenteeIdAndCourseId(final UUID menteeId, final UUID courseId) {
        return toCourseAssignments(courseAssignmentRepository.findByMenteeIdAndCourseId(menteeId, courseId));
    }

    public List<CourseAssignment> findByMentorIdAndCourseId(final UUID mentorId, final UUID courseId) {
        return toCourseAssignments(courseAssignmentRepository.findByMentorIdAndCourseId(mentorId, courseId));
    }

    public List<CourseAssignment> findByCoachIdAndCourseId(final UUID coachId, final UUID courseId) {
        return toCourseAssignments(courseAssignmentRepository.findByCoachIdAndCourseId(coachId, courseId));
    }

    public CourseAssignment findByCourseIdAndMentorIdAndMenteeId(final UUID courseId, final UUID mentorId, final UUID menteeId) {
        return toCourseAssignment(courseAssignmentRepository.findByCourseIdAndMentorIdAndMenteeId(courseId, mentorId, menteeId));
    }

    public List<CourseAssignment> findByMenteeId(final UUID menteeId) {
        return toCourseAssignments(courseAssignmentRepository.findByMenteeId(menteeId));
    }

    public List<CourseAssignment> findByCoachId(final UUID coachId) {
        return toCourseAssignments(courseAssignmentRepository.findByCoachId(coachId));
    }

    public List<CourseAssignment> findByMentorId(final UUID mentorId) {
        return toCourseAssignments(courseAssignmentRepository.findByMentorId(mentorId));
    }

    public List<CourseAssignment> findByCourseId(final UUID courseId) {
        return toCourseAssignments(courseAssignmentRepository.findByCourseId(courseId));
    }

    public List<CourseAssignment> findByCourseIdAndMentorIdIsNullAndMenteeIdIsNotNull(final UUID courseId) {
        return toCourseAssignments(courseAssignmentRepository.findByCourseIdAndMentorIdIsNullAndMenteeIdIsNotNull(courseId));
    }

    public void deleteUnassignedCourseByMentorId(final UUID mentorId) {
        courseAssignmentRepository.deleteUnassignedCourseByMentorId(mentorId);
    }

    public void deleteByCourseId(final UUID courseId) {
        courseAssignmentRepository.deleteByCourseId(courseId);
    }

    public void deleteByMentorIdAndCourseId(final UUID mentorId, final UUID courseId) {
        courseAssignmentRepository.deleteByMentorIdAndCourseId(mentorId, courseId);
    }

    public void deleteByMenteeIdAndMentorIdAndCourseId(final UUID menteeId, final UUID mentorId, final UUID courseId) {
        courseAssignmentRepository.deleteByMenteeIdAndMentorIdAndCourseId(menteeId, mentorId, courseId);
    }

    public void deleteByCourseIdAndMenteeIdWithoutMentorId(final UUID courseId, final UUID menteeId) {
        courseAssignmentRepository.deleteByCourseIdAndMenteeIdWithoutMentorId(courseId, menteeId);
    }

    public void deleteByMenteeIdAndCourseId(final UUID menteeId, final UUID courseId) {
        courseAssignmentRepository.deleteByMenteeIdAndCourseId(menteeId, courseId);
    }
}
