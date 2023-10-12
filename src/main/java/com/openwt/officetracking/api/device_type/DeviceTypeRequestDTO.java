package com.openwt.officetracking.api.device_type;

import com.openwt.officetracking.api.device_configuration.DeviceConfigurationRequestDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@With
public class DeviceTypeRequestDTO {

    private UUID modelId;

    private List<DeviceConfigurationRequestDTO> configurations;
}