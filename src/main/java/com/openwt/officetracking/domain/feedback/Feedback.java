package com.openwt.officetracking.domain.feedback;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@With
@Builder
public class Feedback {

    private UUID id;

    private UUID courseAssignmentId;

    private UUID reviewerId;

    private String firstName;

    private String lastName;

    private String content;

    private boolean isVisible;

    private boolean isMentor;

    private Instant createdAt;
}
