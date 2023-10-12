package com.openwt.officetracking.api.user_mobile;

import com.openwt.officetracking.api.user.UserDetailDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
public class UserMobileDetailDTO {

    private UUID id;

    private String deviceType;

    private String Model;

    private String osVersion;

    private Instant registeredAt;

    private UserDetailDTO owner;
}
