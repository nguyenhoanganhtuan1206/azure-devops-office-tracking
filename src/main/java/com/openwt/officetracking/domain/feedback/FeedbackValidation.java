package com.openwt.officetracking.domain.feedback;

import lombok.experimental.UtilityClass;

import static com.openwt.officetracking.error.CommonError.supplyValidationError;
import static org.apache.commons.lang3.StringUtils.isBlank;

@UtilityClass
public class FeedbackValidation {

    public static void validateFeedback(final FeedbackRequest feedbackRequest) {
        if (isBlank(feedbackRequest.getContent())) {
            throw supplyValidationError("Content feedback cannot be empty").get();
        }

        if (feedbackRequest.getCourseId() == null) {
            throw supplyValidationError("Course ID cannot be empty").get();
        }

        if (feedbackRequest.getMenteeId() == null) {
            throw supplyValidationError("Mentee ID cannot be empty").get();
        }
    }
}
