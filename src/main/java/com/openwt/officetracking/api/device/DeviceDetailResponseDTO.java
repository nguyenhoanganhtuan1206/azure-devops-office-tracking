package com.openwt.officetracking.api.device;

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
public class DeviceDetailResponseDTO {

    private UUID id;

    private UUID modelId;

    private String detail;

    private String serialNumber;

    private Instant fromTimeAt;

    private Instant toTimeAt;

    private Instant purchaseAt;

    private Instant warrantyEndAt;

    private DeviceStatus deviceStatus;

    private DeviceTypeRequest deviceTypeConfig;
}
