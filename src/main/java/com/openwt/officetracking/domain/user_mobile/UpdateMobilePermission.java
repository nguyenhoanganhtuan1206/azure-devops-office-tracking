package com.openwt.officetracking.domain.user_mobile;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

@Getter
@Setter
@Builder
@With
public class UpdateMobilePermission {

    private Boolean isEnableBluetooth;

    private Boolean isEnableLocation;

    private Boolean isEnableBackground;

    private Boolean isEnableNotification;
}
