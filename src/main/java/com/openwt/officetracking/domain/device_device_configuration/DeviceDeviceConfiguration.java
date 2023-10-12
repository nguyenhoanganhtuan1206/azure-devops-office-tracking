package com.openwt.officetracking.domain.device_device_configuration;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.UUID;

@Getter
@Setter
@Builder
@With
public class DeviceDeviceConfiguration {

    private UUID id;

    private UUID deviceId;

    private UUID deviceModelConfigurationValueId;
}
