package com.openwt.officetracking.api.device;

import com.openwt.officetracking.domain.device.DeviceRequestReject;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DeviceRequestRejectDTOMapper {

    public static DeviceRequestReject toDeviceRequestReject(final DeviceRequestRejectDTO deviceRequestRejectDTO) {
        return DeviceRequestReject.builder()
                .requestId(deviceRequestRejectDTO.getRequestId())
                .rejectNote(deviceRequestRejectDTO.getRejectNote())
                .build();
    }
}
