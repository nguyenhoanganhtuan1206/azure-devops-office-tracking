package com.openwt.officetracking.domain.feedback;

import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.course_assignment.CourseAssignmentService;
import com.openwt.officetracking.domain.user.UserService;
import com.openwt.officetracking.error.BadRequestException;
import com.openwt.officetracking.error.NotFoundException;
import com.openwt.officetracking.persistent.course_assignment.CourseAssignmentStore;
import com.openwt.officetracking.persistent.feedback.FeedbackStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.CourseAssignmentFakes.buildCourseAssignment;
import static com.openwt.officetracking.fake.FeedbackFakes.*;
import static com.openwt.officetracking.fake.UserFakes.buildUser;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @Mock
    private FeedbackStore feedbackStore;

    @InjectMocks
    private FeedbackService feedbackService;

    @Mock
    private CourseAssignmentService courseAssignmentService;

    @Mock
    private CourseAssignmentStore courseAssignmentStore;

    @Mock
    private AuthsProvider authsProvider;

    @Mock
    private UserService userService;

    @Test
    void shouldFindFeedbackMentee_OK() {
        final var courseId = randomUUID();
        final var menteeId = randomUUID();
        final var feedback = buildFeedbacks();
        final var courseAssignment = buildCourseAssignment();
        final var user = buildUser();

        when(authsProvider.getCurrentUserRole()).thenReturn("ROLE_ADMIN");
        when(feedbackStore.findFeedbackMenteeInCourseByAdmin(courseId, menteeId)).thenReturn(feedback);
        feedback.forEach(fbItem -> {
            when(courseAssignmentService.findById(fbItem.getCourseAssignmentId())).thenReturn(courseAssignment);
            when(userService.findById(courseAssignment.getMentorId())).thenReturn(user);
        });

        final var actual = feedbackService.findFeedbackMentee(courseId, menteeId);

        assertEquals(feedback.size(), actual.size());
        assertEquals(feedback.get(0).getId(), actual.get(0).getId());
        assertEquals(feedback.get(0).getCourseAssignmentId().toString(), actual.get(0).getCourseAssignmentId().toString());
        assertEquals(feedback.get(0).getContent(), actual.get(0).getContent());
        assertEquals(feedback.get(0).isVisible(), actual.get(0).isVisible());
        assertEquals(feedback.get(0).getCreatedAt().toString(), actual.get(0).getCreatedAt().toString());

        verify(authsProvider).getCurrentUserRole();
        verify(feedbackStore).findFeedbackMenteeInCourseByAdmin(courseId, menteeId);
        feedback.forEach(fbItem -> {
            verify(courseAssignmentService).findById(fbItem.getCourseAssignmentId());
            verify(userService, times(4)).findById(courseAssignment.getMentorId());
        });
    }

    @Test
    public void shouldFindById_Ok() {
        final var feedbackId = randomUUID();
        final var feedback = buildFeedback();

        when(feedbackStore.findById(feedbackId)).thenReturn(Optional.of(feedback));

        final var actual = feedbackService.findById(feedbackId);

        assertEquals(feedback.getId().toString(), actual.getId().toString());
        assertEquals(feedback.getContent(), actual.getContent());
        assertEquals(feedback.isVisible(), actual.isVisible());
        assertEquals(feedback.getCreatedAt().toString(), actual.getCreatedAt().toString());
        assertEquals(feedback.getCourseAssignmentId().toString(), actual.getCourseAssignmentId().toString());

        verify(feedbackStore).findById(feedbackId);
    }

    @Test
    public void shouldFindById_ThrowFeedbackNotFound() {
        final var feedbackId = randomUUID();

        when(feedbackStore.findById(feedbackId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> feedbackService.findById(feedbackId));

        verify(feedbackStore).findById(feedbackId);
    }

    @Test
    void shouldCreateFeedback_OK() {
        final var mentor = buildUser();
        final var courseAssignment = buildCourseAssignment();
        final var feedbackRequest = buildFeedbackRequest()
                .withCourseId(courseAssignment.getCourseId())
                .withMenteeId(courseAssignment.getMenteeId())
                .withCoach(false);
        final var feedback = buildFeedback()
                .withContent(feedbackRequest.getContent())
                .withCourseAssignmentId(courseAssignment.getId());

        when(authsProvider.getCurrentUserId())
                .thenReturn(mentor.getId());
        when(courseAssignmentService.findByMentorMenteeAndCourse(any(), any(), any())).thenReturn(courseAssignment);
        when(feedbackStore.save(argThat(feedbackArg -> feedbackArg.getContent().equals(feedback.getContent()))))
                .thenReturn(feedback);

        final var createdFeedback = feedbackService.create(feedbackRequest);

        assertEquals(feedback, createdFeedback);

        verify(courseAssignmentService).findByMentorMenteeAndCourse(any(), any(), any());
        verify(feedbackStore).save(argThat(feedbackArg -> feedbackArg.getContent().equals(feedback.getContent())));
    }

    @Test
    void shouldCreateFeedback_WithMentorDontHavePermission_ThroughBadRequest() {
        final var mentor = buildUser();
        final var courseAssignment = buildCourseAssignment();
        final var feedbackRequest = buildFeedbackRequest();

        when(authsProvider.getCurrentUserId())
                .thenReturn(mentor.getId());
        when(courseAssignmentService.findByMentorMenteeAndCourse(any(), any(), any())).thenReturn(courseAssignment);

        assertThrows(BadRequestException.class, () -> feedbackService.create(feedbackRequest));

        verify(courseAssignmentService).findByMentorMenteeAndCourse(any(), any(), any());
    }

    @Test
    public void shouldUpdateFeedback_OK() {
        final var feedbackId = randomUUID();
        final var feedbackRequest = buildFeedbackRequest();
        final var feedback = buildFeedback()
                .withId(feedbackId)
                .withContent(feedbackRequest.getContent());

        when(feedbackStore.findById(feedbackId)).thenReturn(Optional.of(feedback));
        when(feedbackStore.save(argThat(feedbackArg -> feedbackArg.getContent().equals(feedback.getContent()))))
                .thenReturn(feedback);

        final var actual = feedbackService.update(feedbackId, feedbackRequest);

        assertEquals(feedback.getId().toString(), actual.getId().toString());
        assertEquals(feedback.getContent(), actual.getContent());
        assertEquals(feedback.getCreatedAt().toString(), actual.getCreatedAt().toString());
        assertEquals(feedback.isVisible(), actual.isVisible());
        assertEquals(feedback.getCourseAssignmentId().toString(), actual.getCourseAssignmentId().toString());

        verify(feedbackStore).findById(feedbackId);
        verify(feedbackStore).save(argThat(feedbackArg -> feedbackArg.getContent().equals(feedback.getContent())));
    }

    @Test
    public void shouldUpdateFeedback_ThrowFeedbackNotFound() {
        final var feedbackId = randomUUID();
        final var feedbackRequest = buildFeedbackRequest();

        when(feedbackStore.findById(feedbackId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> feedbackService.update(feedbackId, feedbackRequest));

        verify(feedbackStore).findById(feedbackId);
    }
}
