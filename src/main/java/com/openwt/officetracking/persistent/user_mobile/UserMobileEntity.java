package com.openwt.officetracking.persistent.user_mobile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Table(name = "user_mobiles")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserMobileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
