package com.openwt.officetracking.persistent.device;


import com.openwt.officetracking.domain.device.Device;
import com.openwt.officetracking.domain.device.DeviceDetail;
import lombok.experimental.UtilityClass;

import java.util.List;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@UtilityClass
public class DeviceEntityMapper {

    public static Device toDevice(final DeviceEntity device) {
        return Device.builder()
                .id(device.getId())
                .modelId(device.getModelId())
                .serialNumber(device.getSerialNumber())
                .deviceRequestId(device.getDeviceRequestId())
                .detail(device.getDetail())
                .deviceStatus(device.getDeviceStatus())
                .assignUserId(device.getAssignUserId())
                .requestUserId(device.getRequestUserId())
                .purchaseAt(device.getPurchaseAt())
                .warrantyEndAt(device.getWarrantyEndAt())
                .createdAt(device.getCreatedAt())
                .lastModifiedAt(device.getLastModifiedAt())
                .requestStatus(device.getRequestStatus())
                .isRequested(device.isRequested())
                .requestNote(device.getNote())
                .requestReason(device.getReason())
                .requestedAt(device.getRequestedAt())
                .acceptedAt(device.getAcceptedAt())
                .rejectedAt(device.getRejectedAt())
                .completedAt(device.getCompletedAt())
                .build();
    }

    public static DeviceEntity toDeviceEntity(final Device device) {
        return DeviceEntity.builder()
                .id(device.getId())
                .modelId(device.getModelId())
                .deviceRequestId(device.getDeviceRequestId())
                .detail(device.getDetail())
                .deviceStatus(device.getDeviceStatus())
                .assignUserId(device.getAssignUserId())
                .requestUserId(device.getRequestUserId())
                .serialNumber(device.getSerialNumber())
                .purchaseAt(device.getPurchaseAt())
                .warrantyEndAt(device.getWarrantyEndAt())
                .createdAt(device.getCreatedAt())
                .lastModifiedAt(device.getLastModifiedAt())
                .requestStatus(device.getRequestStatus())
                .isRequested(device.isRequested())
                .note(device.getRequestNote())
                .reason(device.getRequestReason())
                .requestedAt(device.getRequestedAt())
                .acceptedAt(device.getAcceptedAt())
                .rejectedAt(device.getRejectedAt())
                .completedAt(device.getCompletedAt())
                .build();
    }

    public static List<Device> toDevices(final List<DeviceEntity> deviceEntities) {
        return emptyIfNull(deviceEntities)
                .stream()
                .map(DeviceEntityMapper::toDevice)
                .toList();
    }

    public static DeviceDetail toDeviceDetail(final DeviceEntity deviceEntity) {
        return DeviceDetail.builder()
                .id(deviceEntity.getId())
                .detail(deviceEntity.getDetail())
                .modelId(deviceEntity.getModelId())
                .serialNumber(deviceEntity.getSerialNumber())
                .purchaseAt(deviceEntity.getPurchaseAt())
                .warrantyEndAt(deviceEntity.getWarrantyEndAt())
                .deviceStatus(deviceEntity.getDeviceStatus())
                .build();
    }
}
