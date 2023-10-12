package com.openwt.officetracking.api.auth;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterMobileDTO {

    private String email;

    private String biometryToken;

    private String deviceType;

    private String model;

    private String osVersion;

    private String fcmToken;
}
