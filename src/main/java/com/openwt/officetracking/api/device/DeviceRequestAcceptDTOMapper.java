package com.openwt.officetracking.api.device;

import com.openwt.officetracking.domain.device.DeviceRequestAccept;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DeviceRequestAcceptDTOMapper {

    public static DeviceRequestAccept toDeviceRequestAccept(final DeviceRequestAcceptDTO deviceRequestAcceptDTO) {
        return DeviceRequestAccept.builder()
                .requestId(deviceRequestAcceptDTO.getRequestId())
                .deviceId(deviceRequestAcceptDTO.getDeviceId())
                .build();
    }
}
