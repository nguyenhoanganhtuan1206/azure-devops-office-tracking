package com.openwt.officetracking.domain.device_configuration_value;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.UUID;

@Getter
@Setter
@Builder
@With
public class DeviceConfigurationValueRequest {

    private UUID id;

    private UUID modelId;

    private UUID configurationId;

    private String value;
}
