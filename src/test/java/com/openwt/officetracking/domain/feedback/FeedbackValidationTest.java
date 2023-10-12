package com.openwt.officetracking.domain.feedback;

import com.openwt.officetracking.error.BadRequestException;
import org.junit.jupiter.api.Test;

import static com.openwt.officetracking.domain.feedback.FeedbackValidation.validateFeedback;
import static com.openwt.officetracking.fake.FeedbackFakes.buildFeedbackRequest;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FeedbackValidationTest {

    @Test
    void shouldValidateFeedback_OK() {
        final var feedback = buildFeedbackRequest();

        assertDoesNotThrow(() -> validateFeedback(feedback));
    }

    @Test
    void shouldValidateFeedback_WithoutCourseId_ThroughBadRequest() {
        final var feedback = buildFeedbackRequest()
                .withCourseId(null);

        assertThrows(BadRequestException.class, () -> validateFeedback(feedback));
    }

    @Test
    void shouldValidateFeedback_WithoutMenteeId_ThroughBadRequest() {
        final var feedback = buildFeedbackRequest()
                .withMenteeId(null);

        assertThrows(BadRequestException.class, () -> validateFeedback(feedback));
    }

    @Test
    void shouldValidateFeedback_WithoutContent_ThroughBadRequest() {
        final var feedback = buildFeedbackRequest()
                .withContent(null);

        assertThrows(BadRequestException.class, () -> validateFeedback(feedback));
    }
}
