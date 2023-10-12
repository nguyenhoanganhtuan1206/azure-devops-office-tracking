package com.openwt.officetracking.api.device_type;

import com.openwt.officetracking.api.device_model.DeviceModelDTO;
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
public class DeviceTypeResponseDTO {

    private UUID id;

    private String name;

    private List<DeviceModelDTO> models;
}