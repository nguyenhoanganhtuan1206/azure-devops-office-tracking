package com.openwt.officetracking.api.feedback;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockAdmin;
import com.openwt.officetracking.api.WithMockUser;
import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.feedback.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.openwt.officetracking.fake.FeedbackFakes.*;
import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FeedbackController.class)
@ExtendWith(SpringExtension.class)
class FeedbackControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/feedback";

    @MockBean
    private AuthsProvider authsProvider;

    @MockBean
    private FeedbackService feedbackService;

    @BeforeEach
    void init() {
        when(authsProvider.getCurrentAuthentication()).thenCallRealMethod();
    }

    @Test
    @WithMockAdmin
    void shouldFindFeedbackMentee_OK() throws Exception {
        final var courseId = randomUUID();
        final var menteeId = randomUUID();
        final var feedback = buildFeedbacks();

        when(feedbackService.findFeedbackMentee(courseId, menteeId)).thenReturn(feedback);

        get(BASE_URL + "?courseId=" + courseId + "&menteeId=" + menteeId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(feedback.size()))
                .andExpect(jsonPath("$[0].id").value(feedback.get(0).getId().toString()))
                .andExpect(jsonPath("$[0].courseAssignmentId").value(feedback.get(0).getCourseAssignmentId().toString()))
                .andExpect(jsonPath("$[0].firstName").value(feedback.get(0).getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(feedback.get(0).getLastName()))
                .andExpect(jsonPath("$[0].content").value(feedback.get(0).getContent()))
                .andExpect(jsonPath("$[0].visible").value(feedback.get(0).isVisible()))
                .andExpect(jsonPath("$[0].createdAt").value(feedback.get(0).getCreatedAt().toString()));

        verify(feedbackService).findFeedbackMentee(courseId, menteeId);
    }

    @Test
    @WithMockUser
    void shouldCreateFeedback_OK() throws Exception {
        final var feedback = buildFeedback();
        final var feedbackRequest = buildFeedbackRequest();

        when(feedbackService.create(argThat(fbRequest -> fbRequest.getCourseId().equals(feedbackRequest.getCourseId()) &&
                fbRequest.getMenteeId().equals(feedbackRequest.getMenteeId())))).thenReturn(feedback);

        post(BASE_URL, feedbackRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(feedback.getId().toString()))
                .andExpect(jsonPath("$.courseAssignmentId").value(feedback.getCourseAssignmentId().toString()))
                .andExpect(jsonPath("$.content").value(feedback.getContent()))
                .andExpect(jsonPath("$.visible").value(feedback.isVisible()))
                .andExpect(jsonPath("$.createdAt").value(feedback.getCreatedAt().toString()));

        verify(feedbackService).create(argThat(fbRequest -> fbRequest.getCourseId().equals(feedbackRequest.getCourseId()) &&
                fbRequest.getMenteeId().equals(feedbackRequest.getMenteeId())));
    }

    @Test
    @WithMockUser
    void shouldUpdateFeedback_OK() throws Exception {
        final var feedback = buildFeedback();
        final var feedbackRequest = buildFeedbackRequest();

        when(feedbackService.update(eq(feedback.getId()), argThat(fbRequest -> fbRequest.getCourseId().equals(feedbackRequest.getCourseId()) &&
                fbRequest.getMenteeId().equals(feedbackRequest.getMenteeId())))).thenReturn(feedback);

        put(BASE_URL + "/" + feedback.getId(), feedbackRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(feedback.getId().toString()))
                .andExpect(jsonPath("$.courseAssignmentId").value(feedback.getCourseAssignmentId().toString()))
                .andExpect(jsonPath("$.content").value(feedback.getContent()))
                .andExpect(jsonPath("$.visible").value(feedback.isVisible()))
                .andExpect(jsonPath("$.createdAt").value(feedback.getCreatedAt().toString()));

        verify(feedbackService).update(eq(feedback.getId()), argThat(fbRequest -> fbRequest.getCourseId().equals(feedbackRequest.getCourseId()) &&
                fbRequest.getMenteeId().equals(feedbackRequest.getMenteeId())));
    }

    @Test
    @WithMockUser
    void shouldDeleteFeedback_OK() throws Exception {
        final var feedbackId  = randomUUID();

        doNothing().when(feedbackService).delete(feedbackId);

        delete(BASE_URL + "/" + feedbackId)
                .andExpect(status().isOk());

        verify(feedbackService).delete(feedbackId);
    }
}