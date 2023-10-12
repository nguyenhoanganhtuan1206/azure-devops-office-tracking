package com.openwt.officetracking.api.device;

import com.openwt.officetracking.domain.device.Device;
import com.openwt.officetracking.domain.device.DeviceRequest;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.api.device_type.DeviceTypeDTOMapper.toDeviceTypeRequestDTO;

@UtilityClass
public class DeviceRequestDTOMapper {

    public static DeviceRequestResponseDTO toDeviceRequestResponseDTO(final Device device) {
        return DeviceRequestResponseDTO.builder()
                .id(device.getId())
                .requestUserId(device.getRequestUserId())
                .modelId(device.getModelId())
                .detail(device.getDetail())
                .requestStatus(device.getRequestStatus())
                .note(device.getRequestNote())
                .reason(device.getRequestReason())
                .requestedAt(device.getRequestedAt())
                .acceptedAt(device.getAcceptedAt())
                .rejectedAt(device.getRejectedAt())
                .createdAt(device.getCreatedAt())
                .lastModifiedAt(device.getLastModifiedAt())
                .build();
    }

    public static DeviceRequest toDeviceRequestRequest(final DeviceRequestRequestDTO deviceRequestRequestDTO) {
        return DeviceRequest.builder()
                .deviceTypeConfig(toDeviceTypeRequestDTO(deviceRequestRequestDTO.getDeviceTypeConfig()))
                .condition(deviceRequestRequestDTO.getReason())
                .build();
    }

    public static List<DeviceRequestResponseDTO> toDeviceRequestResponseDTOs(final List<Device> devices) {
        return devices.stream()
                .map(DeviceRequestDTOMapper::toDeviceRequestResponseDTO)
                .toList();
    }
}
