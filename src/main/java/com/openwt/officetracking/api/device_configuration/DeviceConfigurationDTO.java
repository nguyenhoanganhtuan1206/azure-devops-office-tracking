package com.openwt.officetracking.api.device_configuration;

import com.openwt.officetracking.api.device_configuration_value.DeviceConfigurationValueDTO;
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
public class DeviceConfigurationDTO {

    private UUID id;

    private String label;

    private List<DeviceConfigurationValueDTO> values;

}