package com.openwt.officetracking.api.user_mobile;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
public class UserMobileDTO {

    private UUID id;

    private UUID userId;

    private String deviceType;

    private String model;

    private String osVersion;

    private boolean isActive;

    private Instant registeredAt;
}
