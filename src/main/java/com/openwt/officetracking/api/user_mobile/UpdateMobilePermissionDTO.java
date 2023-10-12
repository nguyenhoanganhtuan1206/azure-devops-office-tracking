package com.openwt.officetracking.api.user_mobile;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateMobilePermissionDTO {

    private Boolean isEnableBluetooth;

    private Boolean isEnableLocation;

    private Boolean isEnableBackground;

    private Boolean isEnableNotification;
}
