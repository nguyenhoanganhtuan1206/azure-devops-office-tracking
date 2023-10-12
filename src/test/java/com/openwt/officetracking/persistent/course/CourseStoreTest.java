package com.openwt.officetracking.persistent.course;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.CourseFakes.buildCourseEntities;
import static com.openwt.officetracking.fake.CourseFakes.buildCourseEntity;
import static com.openwt.officetracking.persistent.course.CourseEntityMapper.toCourse;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseStoreTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseStore courseStore;

    @Test
    void shouldFindById_OK() {
        final var course = buildCourseEntity();
        final var courseOptional = Optional.of(course);

        when(courseRepository.findById(course.getId()))
                .thenReturn(Optional.of(course));

        final var expected = courseOptional.get();
        final var actual = courseStore.findById(course.getId()).get();

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
        assertEquals(expected.getEndAt(), actual.getEndAt());
        assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());

        verify(courseRepository).findById(course.getId());
    }

    @Test
    void shouldFindByName_OK() {
        final var course = buildCourseEntity();
        final var courseOptional = Optional.of(course);

        when(courseRepository.findByName(course.getName()))
                .thenReturn(Optional.of(course));

        final var expected = courseOptional.get();
        final var actual = courseStore.findByName(course.getName()).get();

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
        assertEquals(expected.getEndAt(), actual.getEndAt());
        assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());

        verify(courseRepository).findByName(course.getName());
    }

    @Test
    void shouldSaveCourse_OK() {
        final var course = buildCourseEntity();

        when(courseRepository.save(any(CourseEntity.class)))
                .thenReturn(course);

        final var actual = courseStore.save(toCourse(course));

        assertEquals(course.getId(), actual.getId());
        assertEquals(course.getName(), actual.getName());
        assertEquals(course.getDescription(), actual.getDescription());
        assertEquals(course.getCreatedAt(), actual.getCreatedAt());
        assertEquals(course.getEndAt(), actual.getEndAt());
        assertEquals(course.getUpdatedAt(), actual.getUpdatedAt());

        verify(courseRepository).save(any(CourseEntity.class));
    }

    @Test
    void shouldFindAll_OK() {
        final var courses = buildCourseEntities();

        when(courseRepository.findAllByOrderByCreatedAtDesc()).thenReturn(courses);

        final var actual = courseStore.findAll();

        assertEquals(courses.size(), actual.size());

        verify(courseRepository).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void shouldFindByMentorId_OK() {
        final var mentorId = randomUUID();
        final var courses = buildCourseEntities();

        when(courseRepository.findByMentorId(mentorId)).thenReturn(courses);

        final var actual = courseStore.findByMentorId(mentorId);

        assertEquals(courses.get(0).getId().toString(), actual.get(0).getId().toString());
        assertEquals(courses.get(0).getName(), actual.get(0).getName());
        assertEquals(courses.get(0).getStartAt().toString(), actual.get(0).getStartAt().toString());
        assertEquals(courses.get(0).getEndAt().toString(), actual.get(0).getEndAt().toString());
        assertEquals(courses.get(0).getCreatedAt().toString(), actual.get(0).getCreatedAt().toString());
        assertEquals(courses.get(0).getUpdatedAt().toString(), actual.get(0).getUpdatedAt().toString());
        assertEquals(courses.get(0).getDescription(), actual.get(0).getDescription());

        verify(courseRepository).findByMentorId(mentorId);
    }

    @Test
    void shouldDeleteId_OK() {
        final var course = buildCourseEntity();

        doNothing().when(courseRepository).deleteById(course.getId());

        courseStore.deleteById(course.getId());

        verify(courseRepository).deleteById(course.getId());
    }
}
