package com.openwt.officetracking.fake;

import com.openwt.officetracking.api.device.DeviceRequestDTO;
import com.openwt.officetracking.api.device.DeviceRequestRequestDTO;
import com.openwt.officetracking.domain.device.*;
import com.openwt.officetracking.domain.device_status.DeviceStatus;
import com.openwt.officetracking.domain.request_status.RequestStatus;
import com.openwt.officetracking.persistent.device.DeviceEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static com.openwt.officetracking.fake.CommonFakes.randomInstant;
import static com.openwt.officetracking.fake.DeviceTypeFakes.buildDeviceTypeRequest;
import static com.openwt.officetracking.fake.DeviceTypeFakes.buildDeviceTypeRequestDTO;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class DeviceFakes {

    public static DeviceEntity buildDeviceEntity() {
        return DeviceEntity.builder()
                .id(randomUUID())
                .modelId(randomUUID())
                .assignUserId(randomUUID())
                .requestUserId(randomUUID())
                .serialNumber(randomAlphabetic(3, 10))
                .purchaseAt(randomInstant())
                .warrantyEndAt(randomInstant())
                .requestStatus(RequestStatus.PENDING)
                .isRequested(true)
                .note(randomAlphabetic(3, 10))
                .detail(randomAlphabetic(3, 10))
                .purchaseAt(randomInstant())
                .deviceStatus(DeviceStatus.ASSIGNED)
                .warrantyEndAt(randomInstant())
                .requestStatus(RequestStatus.ACCEPTED)
                .isRequested(false)
                .note(randomAlphabetic(3, 10))
                .reason(randomAlphabetic(3, 10))
                .requestedAt(randomInstant())
                .acceptedAt(randomInstant())
                .rejectedAt(randomInstant())
                .createdAt(randomInstant())
                .lastModifiedAt(randomInstant())
                .build();
    }

    public static DeviceDetail buildDeviceDetailResponse() {
        return DeviceDetail.builder()
                .id(randomUUID())
                .modelId(randomUUID())
                .detail(randomAlphabetic(3, 10))
                .serialNumber(randomAlphabetic(3, 10))
                .purchaseAt(randomInstant())
                .warrantyEndAt(randomInstant())
                .deviceStatus(DeviceStatus.ASSIGNED)
                .deviceTypeConfig(buildDeviceTypeRequest())
                .build();
    }

    public static List<DeviceEntity> buildDeviceEntities() {
        return buildList(DeviceFakes::buildDeviceEntity);
    }

    public static Device buildDevice() {
        return Device.builder()
                .id(randomUUID())
                .assignUserId(randomUUID())
                .requestUserId(randomUUID())
                .modelId(randomUUID())
                .serialNumber(randomAlphabetic(3, 10))
                .purchaseAt(randomInstant())
                .warrantyEndAt(randomInstant())
                .requestStatus(RequestStatus.ACCEPTED)
                .isRequested(true)
                .requestNote(randomAlphabetic(3, 10))
                .requestReason(randomAlphabetic(3, 10))
                .detail(randomAlphabetic(3, 10))
                .deviceStatus(DeviceStatus.ASSIGNED)
                .requestedAt(randomInstant())
                .acceptedAt(randomInstant())
                .rejectedAt(randomInstant())
                .createdAt(randomInstant())
                .lastModifiedAt(randomInstant())
                .build();
    }

    public static List<Device> buildDevices() {
        return buildList(DeviceFakes::buildDevice);
    }

    public static DeviceRequestReject buildDeviceRequestReject() {
        return DeviceRequestReject.builder()
                .requestId(randomUUID())
                .rejectNote(randomAlphabetic(3, 10))
                .build();
    }

    public static DeviceRequestAccept buildDeviceRequestAccept() {
        return DeviceRequestAccept.builder()
                .requestId(randomUUID())
                .deviceId(randomUUID())
                .build();
    }

    public static DeviceRequestRequestDTO buildDeviceRequestRequestDTO() {
        return DeviceRequestRequestDTO.builder()
                .deviceTypeConfig(buildDeviceTypeRequestDTO())
                .reason(randomAlphabetic(3, 10))
                .build();
    }

    public static DeviceRequestDTO buildDeviceRequestDTO() {
        return DeviceRequestDTO.builder()
                .serialNumber(randomAlphabetic(3, 10))
                .deviceTypeConfig(buildDeviceTypeRequestDTO())
                .purchaseAt(now().minus(30, DAYS))
                .warrantyEndAt(now().plus(365, DAYS))
                .deviceStatus(DeviceStatus.ASSIGNED)
                .assignUserId(randomUUID())
                .note(randomAlphabetic(3, 10))
                .condition(randomAlphabetic(3, 10))
                .build();
    }

    public static DeviceRequest buildDeviceRequest() {
        return DeviceRequest.builder()
                .serialNumber(randomAlphabetic(3, 10))
                .deviceTypeConfig(buildDeviceTypeRequest())
                .purchaseAt(now().minus(30, DAYS))
                .warrantyEndAt(now().plus(365, DAYS))
                .deviceStatus(DeviceStatus.ASSIGNED)
                .assignUserId(randomUUID())
                .note(randomAlphabetic(3, 10))
                .condition(randomAlphabetic(3, 10))
                .build();
    }
}
