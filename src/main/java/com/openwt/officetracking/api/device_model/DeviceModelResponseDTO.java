package com.openwt.officetracking.api.device_model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.UUID;

@Getter
@Setter
@Builder
@With
public class DeviceModelResponseDTO {

    private UUID id;

    private UUID typeId;

    private String name;
}
