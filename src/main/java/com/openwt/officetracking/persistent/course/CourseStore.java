package com.openwt.officetracking.persistent.course;

import com.openwt.officetracking.domain.course.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.persistent.course.CourseEntityMapper.*;
import static org.apache.commons.collections4.IterableUtils.toList;

@Repository
@RequiredArgsConstructor
public class CourseStore {

    private final CourseRepository courseRepository;

    public Optional<Course> findById(final UUID courseId) {
        return courseRepository.findById(courseId)
                .map(CourseEntityMapper::toCourse);
    }

    public List<Course> findAll() {
        return toCourses(toList(courseRepository.findAllByOrderByCreatedAtDesc()));
    }

    public List<Course> findByMenteeId(final UUID menteeId) {
        return toCourses(toList(courseRepository.findByMenteeId(menteeId)));
    }

    public List<Course> findByMentorId(final UUID mentorId) {
        return toCourses(toList(courseRepository.findByMentorId(mentorId)));
    }

    public List<Course> findByCoachId(final UUID coachId) {
        return toCourses(toList(courseRepository.findByCoachId(coachId)));
    }

    public Course save(final Course course) {
        return toCourse(courseRepository.save(toCourseEntity(course)));
    }

    public Optional<Course> findByName(final String name) {
        return courseRepository.findByName(name)
                .map(CourseEntityMapper::toCourse);
    }

    public List<Course> findCoursesByMenteeId(final UUID menteeId) {
        return toCourses(toList(courseRepository.findActiveCourseByMenteeId(menteeId)));
    }

    public List<Course> findCoursesByMentorId(final UUID mentorId) {
        return toCourses(toList(courseRepository.findByMentorId(mentorId)));
    }

    public void deleteById(final UUID courseId) {
        courseRepository.deleteById(courseId);
    }
}
