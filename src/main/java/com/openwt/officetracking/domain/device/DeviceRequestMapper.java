package com.openwt.officetracking.domain.device;

import com.openwt.officetracking.domain.request_status.RequestStatus;
import lombok.experimental.UtilityClass;

import java.time.Instant;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

@UtilityClass
public class DeviceRequestMapper {

    public static Device toDevice(final DeviceRequest deviceRequest) {
        return Device.builder()
                .modelId(deviceRequest.getDeviceTypeConfig().getModelId())
                .deviceStatus(deviceRequest.getDeviceStatus())
                .requestNote(deviceRequest.getNote())
                .requestReason(deviceRequest.getCondition())
                .serialNumber(deviceRequest.getSerialNumber())
                .purchaseAt(deviceRequest.getPurchaseAt())
                .warrantyEndAt(deviceRequest.getWarrantyEndAt())
                .createdAt(Instant.now())
                .lastModifiedAt(Instant.now())
                .build();
    }

    public static Device toDeviceWhenRequest(final DeviceRequest deviceRequest) {
        return Device.builder()
                .modelId(deviceRequest.getDeviceTypeConfig().getModelId())
                .requestStatus(RequestStatus.PENDING)
                .requestReason(deviceRequest.getCondition())
                .isRequested(true)
                .serialNumber(String.valueOf(randomUUID()))
                .purchaseAt(now())
                .createdAt(now())
                .requestedAt(now())
                .lastModifiedAt(now())
                .build();
    }
}