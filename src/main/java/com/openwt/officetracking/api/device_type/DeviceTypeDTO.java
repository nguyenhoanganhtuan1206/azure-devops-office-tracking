package com.openwt.officetracking.api.device_type;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class DeviceTypeDTO {

    private UUID id;

    private String name;
}