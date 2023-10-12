package com.openwt.officetracking.api.device;

import com.openwt.officetracking.domain.device_status.DeviceStatus;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceResponseDTO {

    private UUID id;

    private String detail;

    private UUID modelId;

    private String serialNumber;

    private Instant purchaseAt;

    private Instant warrantyEndAt;

    private DeviceStatus deviceStatus;

    private UUID assignUserId;

    private String note;

    private String reason;

    private Instant createdAt;

    private Instant lastModifiedAt;
}
