package com.openwt.officetracking.domain.device_model;

import com.openwt.officetracking.domain.device_configuration.DeviceConfiguration;
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
public class DeviceModelResponse {

    private UUID id;

    private String name;

    private List<DeviceConfiguration> configurations;
}
