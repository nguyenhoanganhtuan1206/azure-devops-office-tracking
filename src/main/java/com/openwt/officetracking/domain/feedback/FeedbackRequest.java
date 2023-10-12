package com.openwt.officetracking.domain.feedback;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.UUID;

@Setter
@Getter
@With
@Builder
public class FeedbackRequest {

    private String content;

    private UUID courseId;

    private UUID menteeId;

    private boolean isCoach;

    private boolean isVisible;
}
