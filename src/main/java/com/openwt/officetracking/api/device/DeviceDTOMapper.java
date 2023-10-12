package com.openwt.officetracking.api.device;

import com.openwt.officetracking.domain.device.Device;
import com.openwt.officetracking.domain.device.DeviceRequest;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.api.device_type.DeviceTypeDTOMapper.toDeviceTypeRequestDTO;

@UtilityClass
public class DeviceDTOMapper {

    public static DeviceResponseDTO toDeviceResponseDTO(final Device device) {
        return DeviceResponseDTO.builder()
                .id(device.getId())
                .detail(device.getDetail())
                .modelId(device.getModelId())
                .serialNumber(device.getSerialNumber())
                .purchaseAt(device.getPurchaseAt())
                .warrantyEndAt(device.getWarrantyEndAt())
                .deviceStatus(device.getDeviceStatus())
                .assignUserId(device.getAssignUserId())
                .note(device.getRequestNote())
                .reason(device.getRequestReason())
                .createdAt(device.getCreatedAt())
                .lastModifiedAt(device.getLastModifiedAt())
                .build();
    }

    public static List<DeviceResponseDTO> toDeviceResponseDTOs(final List<Device> devices) {
        return devices.stream()
                .map(DeviceDTOMapper::toDeviceResponseDTO)
                .toList();
    }

    public static DeviceRequest toDeviceRequestDTO(final DeviceRequestDTO deviceRequestDTO) {
        return DeviceRequest.builder()
                .serialNumber(deviceRequestDTO.getSerialNumber())
                .deviceTypeConfig(toDeviceTypeRequestDTO(deviceRequestDTO.getDeviceTypeConfig()))
                .purchaseAt(deviceRequestDTO.getPurchaseAt())
                .warrantyEndAt(deviceRequestDTO.getWarrantyEndAt())
                .deviceStatus(deviceRequestDTO.getDeviceStatus())
                .assignUserId(deviceRequestDTO.getAssignUserId())
                .note(deviceRequestDTO.getNote())
                .condition(deviceRequestDTO.getCondition())
                .deviceRequestId(deviceRequestDTO.getDeviceRequestId())
                .build();
    }
}
