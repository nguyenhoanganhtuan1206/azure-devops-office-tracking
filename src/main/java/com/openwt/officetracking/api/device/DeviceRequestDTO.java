package com.openwt.officetracking.api.device;

import com.openwt.officetracking.api.device_type.DeviceTypeRequestDTO;
import com.openwt.officetracking.domain.device_status.DeviceStatus;
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
public class DeviceRequestDTO {

    private String serialNumber;

    private DeviceTypeRequestDTO deviceTypeConfig;

    private Instant purchaseAt;

    private Instant warrantyEndAt;

    private DeviceStatus deviceStatus;

    private UUID assignUserId;

    private String note;

    private String condition;

    private UUID deviceRequestId;
}