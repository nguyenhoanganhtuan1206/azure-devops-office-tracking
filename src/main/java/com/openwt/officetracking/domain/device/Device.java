package com.openwt.officetracking.domain.device;

import com.openwt.officetracking.domain.device_status.DeviceStatus;
import com.openwt.officetracking.domain.request_status.RequestStatus;
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
public class Device {

    private UUID id;

    private UUID assignUserId;

    private UUID requestUserId;

    private UUID deviceRequestId;

    private String detail;

    private UUID modelId;

    private String serialNumber;

    private Instant purchaseAt;

    private Instant warrantyEndAt;

    private DeviceStatus deviceStatus;

    private boolean isRequested;

    private RequestStatus requestStatus;

    private String requestNote;

    private String requestReason;

    private Instant requestedAt;

    private Instant acceptedAt;

    private Instant rejectedAt;

    private Instant completedAt;

    private Instant createdAt;

    private Instant lastModifiedAt;
}