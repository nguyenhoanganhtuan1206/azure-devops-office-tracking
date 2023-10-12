package com.openwt.officetracking.api.device_configuration_value;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.UUID;

@Getter
@Setter
@Builder
@With
public class DeviceConfigurationValueDTO {

    private UUID id;

    private String value;
}