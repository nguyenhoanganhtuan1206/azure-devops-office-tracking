package com.openwt.officetracking.api.device;

import com.openwt.officetracking.api.device_type.DeviceTypeRequestDTO;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRequestRequestDTO {

    private DeviceTypeRequestDTO deviceTypeConfig;

    private String reason;
}
