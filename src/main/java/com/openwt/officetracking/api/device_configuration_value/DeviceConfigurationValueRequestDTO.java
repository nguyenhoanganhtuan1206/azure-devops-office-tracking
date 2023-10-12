package com.openwt.officetracking.api.device_configuration_value;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceConfigurationValueRequestDTO {

    private UUID modelId;

    private UUID configurationId;

    private String value;
}
