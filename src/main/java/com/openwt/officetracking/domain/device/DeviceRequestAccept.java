package com.openwt.officetracking.domain.device;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.UUID;

@Getter
@Setter
@Builder
@With
public class DeviceRequestAccept {

    private UUID requestId;

    private UUID deviceId;
}
