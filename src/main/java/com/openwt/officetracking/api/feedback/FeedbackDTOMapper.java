package com.openwt.officetracking.api.feedback;

import com.openwt.officetracking.domain.feedback.Feedback;
import com.openwt.officetracking.domain.feedback.FeedbackRequest;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class FeedbackDTOMapper {

    public static FeedbackRequest toFeedbackRequest(final FeedbackRequestDTO feedbackRequestDTO) {
        return FeedbackRequest.builder()
                .courseId(feedbackRequestDTO.getCourseId())
                .menteeId(feedbackRequestDTO.getMenteeId())
                .content(feedbackRequestDTO.getContent())
                .isCoach(feedbackRequestDTO.isCoach())
                .isVisible(feedbackRequestDTO.isVisible())
                .build();
    }

    public static FeedbackResponseDTO toFeedbackResponse(final Feedback feedback) {
        return FeedbackResponseDTO.builder()
                .id(feedback.getId())
                .reviewerId(feedback.getReviewerId())
                .courseAssignmentId(feedback.getCourseAssignmentId())
                .createdAt(feedback.getCreatedAt())
                .content(feedback.getContent())
                .firstName(feedback.getFirstName())
                .lastName(feedback.getLastName())
                .isVisible(feedback.isVisible())
                .build();
    }

    public static List<FeedbackResponseDTO> toFeedbackResponseDTOs(final List<Feedback> feedback) {
        return feedback.stream()
                .map(FeedbackDTOMapper::toFeedbackResponse)
                .toList();
    }
}
