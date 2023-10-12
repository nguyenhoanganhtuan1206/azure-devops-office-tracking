package com.openwt.officetracking.persistent.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Table(name = "feedback")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FeedBackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID courseAssignmentId;

    private String content;

    private boolean isVisible;

    private boolean isMentor;

    private Instant createdAt;
}
