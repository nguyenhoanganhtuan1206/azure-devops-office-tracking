package com.openwt.officetracking.api.device_model;

import com.openwt.officetracking.api.device_configuration.DeviceConfigurationDTO;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceModelDTO {

    private UUID id;

    private String name;

    private List<DeviceConfigurationDTO> configurations;
}
