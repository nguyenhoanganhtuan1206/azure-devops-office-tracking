package com.openwt.officetracking.api.device_type;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.UUID;

@Getter
@Setter
@Builder
@With
public class DeviceConfigurationRequestDTO {

    private UUID configurationId;

    private UUID configurationValueId;
}
