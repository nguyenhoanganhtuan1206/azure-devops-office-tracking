package com.openwt.officetracking.fake;

import com.openwt.officetracking.domain.feedback.Feedback;
import com.openwt.officetracking.domain.feedback.FeedbackRequest;
import com.openwt.officetracking.persistent.feedback.FeedBackEntity;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class FeedbackFakes {

    public static Feedback buildFeedback() {
        return Feedback.builder()
                .id(randomUUID())
                .courseAssignmentId(randomUUID())
                .content(randomAlphabetic(9))
                .isVisible(false)
                .isMentor(true)
                .createdAt(Instant.now())
                .build();
    }

    public static List<Feedback> buildFeedbacks() {
        return buildList(FeedbackFakes::buildFeedback);
    }

    public static FeedBackEntity buildFeedbackEntity() {
        return FeedBackEntity.builder()
                .id(randomUUID())
                .courseAssignmentId(randomUUID())
                .content(randomAlphabetic(9))
                .createdAt(Instant.now())
                .build();
    }

    public static List<FeedBackEntity> buildFeedbackEntities() {
        return buildList(FeedbackFakes::buildFeedbackEntity);
    }

    public static FeedbackRequest buildFeedbackRequest() {
        return FeedbackRequest.builder()
                .courseId(randomUUID())
                .menteeId(randomUUID())
                .content(randomAlphabetic(9))
                .isCoach(false)
                .isVisible(true)
                .build();
    }
}
