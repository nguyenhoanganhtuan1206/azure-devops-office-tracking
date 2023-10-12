package com.openwt.officetracking.domain.device;

import com.openwt.officetracking.domain.device_status.DeviceStatus;
import com.openwt.officetracking.domain.device_type.DeviceTypeRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@With
public class DeviceRequest {

    private String serialNumber;

    private DeviceTypeRequest deviceTypeConfig;

    private Instant purchaseAt;

    private Instant warrantyEndAt;

    private DeviceStatus deviceStatus;

    private UUID assignUserId;

    private String note;

    private String condition;

    private UUID deviceRequestId;
}