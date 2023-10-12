package com.openwt.officetracking.api.feedback;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequestDTO {

    private String content;

    private UUID courseId;

    private UUID menteeId;

    private boolean isCoach;

    private boolean isVisible;
}
