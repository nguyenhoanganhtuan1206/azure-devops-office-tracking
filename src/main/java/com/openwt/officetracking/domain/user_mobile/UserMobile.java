package com.openwt.officetracking.domain.user_mobile;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@With
public class UserMobile {

    private UUID id;

    private UUID userId;

    private String deviceType;

    private String model;

    private String osVersion;

    private String biometryToken;

    private String fcmToken;

    private boolean isActive;

    private Instant registeredAt;

    private boolean isEnableBluetooth;

    private boolean isEnableLocation;

    private boolean isEnableBackground;

    private boolean isEnableNotification;
}
