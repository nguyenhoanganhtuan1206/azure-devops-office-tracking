package com.openwt.officetracking.api.user_mobile;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserMobileDTO {

    private String model;

    private String deviceType;

    private String osVersion;

    private String fcmToken;
}
