package com.openwt.officetracking.domain.device_configuration;

import com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValue;
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
public class DeviceConfiguration {

    private UUID id;

    private String label;

    private UUID deviceTypeId;

    private List<DeviceConfigurationValue> values;
}
