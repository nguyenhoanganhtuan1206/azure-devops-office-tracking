package com.openwt.officetracking.persistent.course_assignment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.CourseAssignmentFakes.buildCourseAssignmentEntities;
import static com.openwt.officetracking.fake.CourseAssignmentFakes.buildCourseAssignmentEntity;
import static com.openwt.officetracking.persistent.course_assignment.CourseAssignmentEntityMapper.toCourseAssignment;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseAssignmentStoreTest {

    @Mock
    private CourseAssignmentRepository courseAssignmentRepository;

    @InjectMocks
    private CourseAssignmentStore courseAssignmentStore;

    @Test
    void shouldSave_OK() {
        final var courseAssignment = buildCourseAssignmentEntity();

        when(courseAssignmentRepository.save(any()))
                .thenReturn(courseAssignment);

        final var actual = courseAssignmentStore.save(toCourseAssignment(courseAssignment));

        assertEquals(courseAssignment.getId(), actual.getId());
        assertEquals(courseAssignment.getCourseId(), actual.getCourseId());
        assertEquals(courseAssignment.getMentorId(), actual.getMentorId());
        assertEquals(courseAssignment.getMenteeId(), actual.getMenteeId());

        verify(courseAssignmentRepository).save(any());
    }

    @Test
    void shouldFindByMentorId_OK() {
        final var id = randomUUID();
        final var courseAssignments = buildCourseAssignmentEntities();

        when(courseAssignmentRepository.findByMentorId(id))
                .thenReturn(courseAssignments);

        final var actual = courseAssignmentStore.findByMentorId(id);

        assertEquals(courseAssignments.get(0).getId().toString(), actual.get(0).getId().toString());
        assertEquals(courseAssignments.get(0).getCourseId().toString(), actual.get(0).getCourseId().toString());
        assertEquals(courseAssignments.get(0).getMentorId().toString(), actual.get(0).getMentorId().toString());
        assertEquals(courseAssignments.get(0).getMenteeId().toString(), actual.get(0).getMenteeId().toString());

        verify(courseAssignmentRepository).findByMentorId(id);
    }

    @Test
    void shouldFindByMenteeId_OK() {
        final var id = randomUUID();
        final var courseAssignments = buildCourseAssignmentEntities();

        when(courseAssignmentRepository.findByMenteeId(id))
                .thenReturn(courseAssignments);

        final var actual = courseAssignmentStore.findByMenteeId(id);

        assertEquals(courseAssignments.get(0).getId().toString(), actual.get(0).getId().toString());
        assertEquals(courseAssignments.get(0).getCourseId().toString(), actual.get(0).getCourseId().toString());
        assertEquals(courseAssignments.get(0).getMentorId().toString(), actual.get(0).getMentorId().toString());
        assertEquals(courseAssignments.get(0).getMenteeId().toString(), actual.get(0).getMenteeId().toString());

        verify(courseAssignmentRepository).findByMenteeId(id);
    }

    @Test
    void shouldFindByCourseId_OK() {
        final var id = randomUUID();
        final var courseAssignments = buildCourseAssignmentEntities();

        when(courseAssignmentRepository.findByCourseId(id))
                .thenReturn(courseAssignments);

        final var actual = courseAssignmentStore.findByCourseId(id);

        assertEquals(courseAssignments.get(0).getId().toString(), actual.get(0).getId().toString());
        assertEquals(courseAssignments.get(0).getCourseId().toString(), actual.get(0).getCourseId().toString());
        assertEquals(courseAssignments.get(0).getMentorId().toString(), actual.get(0).getMentorId().toString());
        assertEquals(courseAssignments.get(0).getMenteeId().toString(), actual.get(0).getMenteeId().toString());

        verify(courseAssignmentRepository).findByCourseId(id);
    }

    @Test
    void shouldFindByMenteeIdAndCourseId_OK() {
        final var menteeId = randomUUID();
        final var courseId = randomUUID();
        final var courseAssignments = buildCourseAssignmentEntities();

        when(courseAssignmentRepository.findByMenteeIdAndCourseId(menteeId, courseId))
                .thenReturn(courseAssignments);

        final var actual = courseAssignmentStore.findByMenteeIdAndCourseId(menteeId, courseId);

        assertEquals(courseAssignments.get(0).getId().toString(), actual.get(0).getId().toString());
        assertEquals(courseAssignments.get(0).getCourseId().toString(), actual.get(0).getCourseId().toString());
        assertEquals(courseAssignments.get(0).getMentorId().toString(), actual.get(0).getMentorId().toString());
        assertEquals(courseAssignments.get(0).getMenteeId().toString(), actual.get(0).getMenteeId().toString());

        verify(courseAssignmentRepository).findByMenteeIdAndCourseId(menteeId, courseId);
    }

    @Test
    void shouldFindByMentorMenteeAndCourse_OK() {
        final var menteeId = randomUUID();
        final var mentorId = randomUUID();
        final var courseId = randomUUID();
        final var courseAssignment = buildCourseAssignmentEntity();

        when(courseAssignmentRepository.findByMentorIdAndMenteeIdAndCourseId(mentorId, menteeId, courseId))
                .thenReturn(Optional.of(courseAssignment));

        final var actual = courseAssignmentRepository.findByMentorIdAndMenteeIdAndCourseId(mentorId, menteeId, courseId).get();

        assertEquals(courseAssignment.getId(), actual.getId());
        assertEquals(courseAssignment.getCourseId(), actual.getCourseId());
        assertEquals(courseAssignment.getMentorId(), actual.getMentorId());
        assertEquals(courseAssignment.getMenteeId(), actual.getMenteeId());

        verify(courseAssignmentRepository).findByMentorIdAndMenteeIdAndCourseId(mentorId, menteeId, courseId);
    }

    @Test
    void shouldFindByCourseIdAndMentorIdIsNullAndMenteeIdIsNotNull_OK() {
        final var courseId = randomUUID();
        final var courseAssignments = buildCourseAssignmentEntities();

        when(courseAssignmentRepository.findByCourseIdAndMentorIdIsNullAndMenteeIdIsNotNull(courseId))
                .thenReturn(courseAssignments);

        final var actual = courseAssignmentRepository.findByCourseIdAndMentorIdIsNullAndMenteeIdIsNotNull(courseId);

        assertEquals(courseAssignments.get(0).getId(), actual.get(0).getId());
        assertEquals(courseAssignments.get(0).getMenteeId(), actual.get(0).getMenteeId());
        assertEquals(courseAssignments.get(0).getMentorId(), actual.get(0).getMentorId());

        verify(courseAssignmentRepository).findByCourseIdAndMentorIdIsNullAndMenteeIdIsNotNull(courseId);
    }

    @Test
    void shouldDeleteUnassignedCourseByMentorId_OK() {
        final var mentorId = randomUUID();

        doNothing().when(courseAssignmentRepository).deleteUnassignedCourseByMentorId(mentorId);

        courseAssignmentRepository.deleteUnassignedCourseByMentorId(mentorId);

        verify(courseAssignmentRepository).deleteUnassignedCourseByMentorId(mentorId);
    }

    @Test
    public void shouldDeleteByCourseId_OK() {
        final var courseId = randomUUID();

        doNothing().when(courseAssignmentRepository).deleteByCourseId(courseId);

        courseAssignmentStore.deleteByCourseId(courseId);

        verify(courseAssignmentRepository).deleteByCourseId(courseId);
    }

    @Test
    void shouldDeleteByMentorIdAndCourseId_OK() {
        final var mentorId = randomUUID();
        final var courseId = randomUUID();

        doNothing().when(courseAssignmentRepository).deleteByMentorIdAndCourseId(mentorId, courseId);

        courseAssignmentRepository.deleteByMentorIdAndCourseId(mentorId, courseId);

        verify(courseAssignmentRepository).deleteByMentorIdAndCourseId(mentorId, courseId);
    }

    @Test
    void shouldDeleteByMenteeIdAndMentorIdAndCourseId_OK() {
        final var mentorId = randomUUID();
        final var menteeId = randomUUID();
        final var courseId = randomUUID();

        doNothing().when(courseAssignmentRepository).deleteByMenteeIdAndMentorIdAndCourseId(mentorId, menteeId, courseId);

        courseAssignmentRepository.deleteByMenteeIdAndMentorIdAndCourseId(mentorId, menteeId, courseId);

        verify(courseAssignmentRepository).deleteByMenteeIdAndMentorIdAndCourseId(mentorId, menteeId, courseId);
    }

    @Test
    void shouldDeleteByCourseIdAndMenteeIdWithoutMentorId_OK() {
        final var menteeId = randomUUID();
        final var courseId = randomUUID();

        doNothing().when(courseAssignmentRepository).deleteByCourseIdAndMenteeIdWithoutMentorId(courseId, menteeId);

        courseAssignmentRepository.deleteByCourseIdAndMenteeIdWithoutMentorId(courseId, menteeId);

        verify(courseAssignmentRepository).deleteByCourseIdAndMenteeIdWithoutMentorId(courseId, menteeId);
    }

    @Test
    void shouldDeleteByMenteeIdAndCourseId_OK() {
        final var menteeId = randomUUID();
        final var courseId = randomUUID();

        doNothing().when(courseAssignmentRepository).deleteByMenteeIdAndCourseId(courseId, menteeId);

        courseAssignmentRepository.deleteByMenteeIdAndCourseId(courseId, menteeId);

        verify(courseAssignmentRepository).deleteByMenteeIdAndCourseId(courseId, menteeId);
    }
}
