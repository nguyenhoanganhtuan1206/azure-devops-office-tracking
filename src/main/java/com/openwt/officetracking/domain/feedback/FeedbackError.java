package com.openwt.officetracking.domain.feedback;

import com.openwt.officetracking.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class FeedbackError {

    public static Supplier<NotFoundException> supplyFeedbackNotFound(final String fieldName, final Object fieldValue) {
        return () -> new NotFoundException("Feedback with %s %s not found", fieldName, fieldValue);
    }
}
