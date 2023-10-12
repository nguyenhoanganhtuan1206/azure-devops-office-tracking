package com.openwt.officetracking.domain.device_type;

import com.openwt.officetracking.domain.device_model.DeviceModelResponse;
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
public class DeviceTypeResponse {

    private UUID id;

    private String name;

    private List<DeviceModelResponse> models;
}