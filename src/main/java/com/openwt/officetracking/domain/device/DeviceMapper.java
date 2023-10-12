package com.openwt.officetracking.domain.device;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class DeviceMapper {

    public static DeviceDetail toDeviceDetail(final Device device) {
        return DeviceDetail.builder()
                .id(device.getId())
                .modelId(device.getModelId())
                .detail(device.getDetail())
                .serialNumber(device.getSerialNumber())
                .purchaseAt(device.getPurchaseAt())
                .warrantyEndAt(device.getWarrantyEndAt())
                .deviceStatus(device.getDeviceStatus())
                .build();
    }

    public static List<DeviceDetail> toDeviceDetails(final List<Device> devices) {
        return devices.stream()
                .map(DeviceMapper::toDeviceDetail)
                .toList();
    }
}
