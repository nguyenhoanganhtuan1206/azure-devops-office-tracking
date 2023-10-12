package com.openwt.officetracking.api.feedback;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@Builder
public class FeedbackResponseDTO {

    private UUID id;

    private UUID courseAssignmentId;

    private UUID reviewerId;

    private String firstName;

    private String lastName;

    private String content;

    private boolean isVisible;

    private Instant createdAt;
}
