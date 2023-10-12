package com.openwt.officetracking.persistent.feedback;

import com.openwt.officetracking.domain.feedback.Feedback;
import lombok.experimental.UtilityClass;

import java.util.List;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@UtilityClass
public class FeedbackEntityMapper {

    public static Feedback toFeedback(final FeedBackEntity feedBackEntity) {
        return Feedback.builder()
                .id(feedBackEntity.getId())
                .content(feedBackEntity.getContent())
                .isVisible(feedBackEntity.isVisible())
                .isMentor(feedBackEntity.isMentor())
                .createdAt(feedBackEntity.getCreatedAt())
                .courseAssignmentId(feedBackEntity.getCourseAssignmentId())
                .build();
    }

    public static List<Feedback> toFeedbacks(final List<FeedBackEntity> feedBackEntities) {
        return emptyIfNull(feedBackEntities)
                .stream()
                .map(FeedbackEntityMapper::toFeedback)
                .toList();
    }

    public static FeedBackEntity toFeedbackEntity(final Feedback feedback) {
        return FeedBackEntity.builder()
                .id(feedback.getId())
                .content(feedback.getContent())
                .isVisible(feedback.isVisible())
                .isMentor(feedback.isMentor())
                .createdAt(feedback.getCreatedAt())
                .courseAssignmentId(feedback.getCourseAssignmentId())
                .build();
    }
}
