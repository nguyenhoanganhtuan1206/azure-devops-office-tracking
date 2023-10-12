package com.openwt.officetracking.domain.course_assignment;

import com.openwt.officetracking.persistent.course_assignment.CourseAssignmentStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.openwt.officetracking.fake.CourseAssignmentFakes.buildCourseAssignment;
import static com.openwt.officetracking.fake.CourseAssignmentFakes.buildCourseAssignments;
import static com.openwt.officetracking.fake.UserFakes.buildUser;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseAssignmentServiceTest {

    @Mock
    private CourseAssignmentStore courseAssignmentStore;

    @InjectMocks
    private CourseAssignmentService courseAssignmentService;

    @Test
    public void shouldCreateCourseAssignment_OK() {
        final var courseAssignment = buildCourseAssignment();

        when(courseAssignmentStore.save(courseAssignment)).thenReturn(courseAssignment);

        courseAssignmentService.create(courseAssignment);

        verify(courseAssignmentStore).save(courseAssignment);
    }

    @Test
    void shouldFindByMenteeId_OK() {
        final var courseAssignments = buildCourseAssignments();
        final var user = buildUser();

        when(courseAssignmentStore.findByMenteeId(user.getId()))
                .thenReturn(courseAssignments);

        final var actual = courseAssignmentService.findByMenteeId(user.getId());

        assertEquals(courseAssignments.size(), actual.size());
        assertEquals(courseAssignments.get(0).getId(), actual.get(0).getId());
        assertEquals(courseAssignments.get(0).getCourseId(), actual.get(0).getCourseId());
        assertEquals(courseAssignments.get(0).getMentorId(), actual.get(0).getMentorId());
        assertEquals(courseAssignments.get(0).getMenteeId(), actual.get(0).getMenteeId());

        verify(courseAssignmentStore).findByMenteeId(user.getId());
    }

    @Test
    void shouldFindByMentorId_OK() {
        final var courseAssignments = buildCourseAssignments();
        final var user = buildUser();

        when(courseAssignmentStore.findByMentorId(user.getId()))
                .thenReturn(courseAssignments);

        final var actual = courseAssignmentService.findByMentorId(user.getId());

        assertEquals(courseAssignments.size(), actual.size());
        assertEquals(courseAssignments.get(0).getId(), actual.get(0).getId());
        assertEquals(courseAssignments.get(0).getCourseId(), actual.get(0).getCourseId());
        assertEquals(courseAssignments.get(0).getMentorId(), actual.get(0).getMentorId());
        assertEquals(courseAssignments.get(0).getMenteeId(), actual.get(0).getMenteeId());

        verify(courseAssignmentStore).findByMentorId(user.getId());
    }

    @Test
    void shouldFindByMentorIdAndCourseId_OK() {
        final var mentorId = randomUUID();
        final var courseId = randomUUID();
        final var courseAssignments = buildCourseAssignments();

        when(courseAssignmentStore.findByMentorIdAndCourseId(mentorId, courseId))
                .thenReturn(courseAssignments);

        final var actual = courseAssignmentService.findByMentorIdAndCourseId(mentorId, courseId);

        assertEquals(courseAssignments.size(), actual.size());
        assertEquals(courseAssignments.get(0).getId(), actual.get(0).getId());
        assertEquals(courseAssignments.get(0).getCourseId(), actual.get(0).getCourseId());
        assertEquals(courseAssignments.get(0).getMentorId(), actual.get(0).getMentorId());
        assertEquals(courseAssignments.get(0).getMenteeId(), actual.get(0).getMenteeId());

        verify(courseAssignmentStore).findByMentorIdAndCourseId(mentorId, courseId);
    }

    @Test
    void shouldFindByMenteeIdAndCourseId_OK() {
        final var menteeId = randomUUID();
        final var courseId = randomUUID();
        final var courseAssignments = buildCourseAssignments();

        when(courseAssignmentStore.findByMenteeIdAndCourseId(menteeId, courseId))
                .thenReturn(courseAssignments);

        final var actual = courseAssignmentService.findByMenteeIdAndCourseId(menteeId, courseId);

        assertEquals(courseAssignments.size(), actual.size());
        assertEquals(courseAssignments.get(0).getId(), actual.get(0).getId());
        assertEquals(courseAssignments.get(0).getCourseId(), actual.get(0).getCourseId());
        assertEquals(courseAssignments.get(0).getMentorId(), actual.get(0).getMentorId());
        assertEquals(courseAssignments.get(0).getMenteeId(), actual.get(0).getMenteeId());

        verify(courseAssignmentStore).findByMenteeIdAndCourseId(menteeId, courseId);
    }


    @Test
    void shouldFindByCourseIdAndMentorIdIsNullAndMenteeIdIsNotNull_OK() {
        final var courseId = randomUUID();
        final var courseAssignments = buildCourseAssignments();

        when(courseAssignmentStore.findByCourseIdAndMentorIdIsNullAndMenteeIdIsNotNull(courseId))
                .thenReturn(courseAssignments);

        final var actual = courseAssignmentService.findByCourseIdAndMentorIdIsNullAndMenteeIdIsNotNull(courseId);

        assertEquals(courseAssignments.size(), actual.size());
        assertEquals(courseAssignments.get(0).getId(), actual.get(0).getId());
        assertEquals(courseAssignments.get(0).getCourseId(), actual.get(0).getCourseId());
        assertEquals(courseAssignments.get(0).getMentorId(), actual.get(0).getMentorId());
        assertEquals(courseAssignments.get(0).getMenteeId(), actual.get(0).getMenteeId());

        verify(courseAssignmentStore).findByCourseIdAndMentorIdIsNullAndMenteeIdIsNotNull(courseId);
    }

    @Test
    void shouldDeleteByCourseId_OK() {
        final var courseId = randomUUID();

        doNothing().when(courseAssignmentStore).deleteByCourseId(courseId);

        courseAssignmentService.deleteByCourseId(courseId);

        verify(courseAssignmentStore).deleteByCourseId(courseId);
    }

    @Test
    void shouldDeleteByMenteeIdAndCourseId_OK() {
        final var courseId = randomUUID();
        final var menteeId = randomUUID();

        doNothing().when(courseAssignmentStore).deleteByMenteeIdAndCourseId(courseId, menteeId);

        courseAssignmentService.deleteByMenteeIdAndCourseId(courseId, menteeId);

        verify(courseAssignmentStore).deleteByMenteeIdAndCourseId(courseId, menteeId);
    }

    @Test
    void shouldDeleteByMentorIdAndCourseId_OK() {
        final var courseId = randomUUID();
        final var mentorId = randomUUID();

        doNothing().when(courseAssignmentStore).deleteByMentorIdAndCourseId(mentorId, courseId);

        courseAssignmentService.deleteByMentorIdAndCourseId(mentorId, courseId);

        verify(courseAssignmentStore).deleteByMentorIdAndCourseId(mentorId, courseId);
    }

    @Test
    void shouldDeleteByMenteeIdAndMentorIdAndCourseId_OK() {
        final var courseId = randomUUID();
        final var mentorId = randomUUID();
        final var menteeId = randomUUID();

        doNothing().when(courseAssignmentStore).deleteByMenteeIdAndMentorIdAndCourseId(menteeId, mentorId, courseId);

        courseAssignmentService.deleteByMenteeIdAndMentorIdAndCourseId(menteeId, mentorId, courseId);

        verify(courseAssignmentStore).deleteByMenteeIdAndMentorIdAndCourseId(menteeId, mentorId, courseId);
    }

    @Test
    void shouldDeleteByCourseIdAndMenteeIdWithoutMentorId_OK() {
        final var courseId = randomUUID();
        final var menteeId = randomUUID();

        doNothing().when(courseAssignmentStore).deleteByCourseIdAndMenteeIdWithoutMentorId(courseId, menteeId);

        courseAssignmentService.deleteByCourseIdAndMenteeIdWithoutMentorId(courseId, menteeId);

        verify(courseAssignmentStore).deleteByCourseIdAndMenteeIdWithoutMentorId(courseId, menteeId);
    }
}
