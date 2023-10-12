package com.openwt.officetracking.domain.device_type;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.UUID;

@Getter
@Setter
@Builder
@With
public class DeviceType {

    private UUID id;

    private String name;
}
