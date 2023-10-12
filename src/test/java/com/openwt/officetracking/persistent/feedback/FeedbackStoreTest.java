package com.openwt.officetracking.persistent.feedback;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.FeedbackFakes.buildFeedbackEntities;
import static com.openwt.officetracking.fake.FeedbackFakes.buildFeedbackEntity;
import static com.openwt.officetracking.persistent.feedback.FeedbackEntityMapper.toFeedback;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackStoreTest {

    @Mock
    private FeedBackRepository feedBackRepository;

    @InjectMocks
    private FeedbackStore feedbackStore;

    @Test
    void shouldFindByMenteeIdAndCourseId_OK() {
        final var menteeId = randomUUID();
        final var courseId = randomUUID();
        final var feedbacks = buildFeedbackEntities();

        when(feedBackRepository.findByCourseIdAndMenteeId(courseId, menteeId))
                .thenReturn(feedbacks);

        final var actual = feedbackStore.findFeedbackMenteeInCourseByAdmin(courseId, menteeId);

        assertEquals(feedbacks.get(0).getId().toString(), actual.get(0).getId().toString());
        assertEquals(feedbacks.get(0).getCourseAssignmentId().toString(), actual.get(0).getCourseAssignmentId().toString());
        assertEquals(feedbacks.get(0).getContent(), actual.get(0).getContent());
        assertEquals(feedbacks.get(0).isVisible(), actual.get(0).isVisible());
        assertEquals(feedbacks.get(0).getCreatedAt().toString(), actual.get(0).getCreatedAt().toString());

        verify(feedBackRepository).findByCourseIdAndMenteeId(courseId, menteeId);
    }

    @Test
    void shouldFindByMentorIdAndMenteeIdAndCourseId_OK() {
        final var menteeId = randomUUID();
        final var courseId = randomUUID();
        final var mentorId = randomUUID();
        final var feedbacks = buildFeedbackEntities();

        when(feedBackRepository.findByCourseIdAndMentorIdAndMenteeId(courseId, mentorId, menteeId))
                .thenReturn(feedbacks);

        final var actual = feedbackStore.findFeedbackMenteeInCourseByMentor(courseId, mentorId, menteeId);

        assertEquals(feedbacks.get(0).getId().toString(), actual.get(0).getId().toString());
        assertEquals(feedbacks.get(0).getCourseAssignmentId().toString(), actual.get(0).getCourseAssignmentId().toString());
        assertEquals(feedbacks.get(0).getContent(), actual.get(0).getContent());
        assertEquals(feedbacks.get(0).isVisible(), actual.get(0).isVisible());
        assertEquals(feedbacks.get(0).getCreatedAt().toString(), actual.get(0).getCreatedAt().toString());

        verify(feedBackRepository).findByCourseIdAndMentorIdAndMenteeId(courseId, mentorId, menteeId);
    }

    @Test
    public void shouldFindById_OK() {
        final var feedback = buildFeedbackEntity();

        when(feedBackRepository.findById(feedback.getId())).thenReturn(Optional.of(feedback));

        final var actual = feedbackStore.findById(feedback.getId()).get();

        assertEquals(feedback.getId().toString(), actual.getId().toString());
        assertEquals(feedback.getContent(), actual.getContent());
        assertEquals(feedback.getCreatedAt().toString(), actual.getCreatedAt().toString());
        assertEquals(feedback.isVisible(), actual.isVisible());
        assertEquals(feedback.getCourseAssignmentId().toString(), actual.getCourseAssignmentId().toString());

        verify(feedBackRepository).findById(feedback.getId());
    }

    @Test
    public void shouldSave_OK() {
        final var feedback = buildFeedbackEntity();

        when(feedBackRepository.save(any(FeedBackEntity.class))).thenReturn(feedback);

        final var actual = feedbackStore.save(toFeedback(feedback));

        assertEquals(feedback.getId().toString(), actual.getId().toString());
        assertEquals(feedback.getContent(), actual.getContent());
        assertEquals(feedback.getCreatedAt().toString(), actual.getCreatedAt().toString());
        assertEquals(feedback.isVisible(), actual.isVisible());
        assertEquals(feedback.getCourseAssignmentId().toString(), actual.getCourseAssignmentId().toString());

        verify(feedBackRepository).save(any(FeedBackEntity.class));
    }

    @Test
    public void shouldDelete_OK() {
        final var feedbackId = randomUUID();

        doNothing().when(feedBackRepository).deleteById(feedbackId);

        feedbackStore.delete(feedbackId);

        verify(feedBackRepository).deleteById(feedbackId);
    }
}