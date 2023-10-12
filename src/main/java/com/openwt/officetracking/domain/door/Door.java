package com.openwt.officetracking.domain.door;

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
public class Door {

    private UUID id;

    private String name;

    private int major;

    private int minor;

    private String note;

    private UUID officeId;

    private Instant createdAt;
}