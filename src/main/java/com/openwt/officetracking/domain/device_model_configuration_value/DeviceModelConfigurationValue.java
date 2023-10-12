package com.openwt.officetracking.domain.device_model_configuration_value;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.UUID;

@Getter
@Setter
@Builder
@With
public class DeviceModelConfigurationValue {

    private UUID id;

    private UUID deviceModelId;

    private UUID deviceConfigurationId;

    private UUID deviceConfigurationValueId;
}
