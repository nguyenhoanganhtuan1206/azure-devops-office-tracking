package com.openwt.officetracking.api.device_model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceModelRequestDTO {

    private UUID typeId;

    private String name;
}
