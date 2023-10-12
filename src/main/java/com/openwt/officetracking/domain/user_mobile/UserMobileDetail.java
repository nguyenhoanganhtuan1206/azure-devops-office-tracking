package com.openwt.officetracking.domain.user_mobile;

import com.openwt.officetracking.domain.user.UserDetail;
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
public class UserMobileDetail {

    private UUID id;

    private String deviceType;

    private String Model;

    private String osVersion;

    private Instant registeredAt;

    private UserDetail owner;
}
