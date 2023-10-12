package com.openwt.officetracking.domain.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

@Getter
@Setter
@Builder
@With
public class RegisterMobile {

    private String email;

    private String biometryToken;

    private String deviceType;

    private String model;

    private String osVersion;

    private String fcmToken;
}
