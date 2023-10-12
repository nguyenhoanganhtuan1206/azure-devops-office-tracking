package com.openwt.officetracking.domain.device;

import com.openwt.officetracking.domain.device_status.DeviceStatus;
import com.openwt.officetracking.domain.device_type.DeviceTypeRequest;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDetail {

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
