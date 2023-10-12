package com.openwt.officetracking.api.device;

import com.openwt.officetracking.domain.device.DeviceDetail;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class DeviceResponseDTOMapper {

    public static DeviceDetailResponseDTO toDeviceDetailResponseDTO(final DeviceDetail deviceDetail) {
        return DeviceDetailResponseDTO.builder()
                .id(deviceDetail.getId())
                .modelId(deviceDetail.getModelId())
                .detail(deviceDetail.getDetail())
                .fromTimeAt(deviceDetail.getFromTimeAt())
                .toTimeAt(deviceDetail.getToTimeAt())
                .serialNumber(deviceDetail.getSerialNumber())
                .purchaseAt(deviceDetail.getPurchaseAt())
                .warrantyEndAt(deviceDetail.getWarrantyEndAt())
                .deviceStatus(deviceDetail.getDeviceStatus())
                .deviceTypeConfig(deviceDetail.getDeviceTypeConfig())
                .build();
    }

    public static List<DeviceDetailResponseDTO> toDeviceDetailResponseDTOs(final List<DeviceDetail> deviceDetails) {
        return deviceDetails.stream()
                .map(DeviceResponseDTOMapper::toDeviceDetailResponseDTO)
                .toList();
    }
}
