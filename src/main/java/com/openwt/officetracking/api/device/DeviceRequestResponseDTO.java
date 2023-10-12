package com.openwt.officetracking.api.device;

import com.openwt.officetracking.domain.request_status.RequestStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
public class DeviceRequestResponseDTO {

    private UUID id;

    private UUID requestUserId;

    private UUID modelId;

    private String detail;

    private RequestStatus requestStatus;

    private String note;

    private String reason;

    private Instant requestedAt;

    private Instant acceptedAt;

    private Instant rejectedAt;

    private Instant createdAt;

    private Instant lastModifiedAt;
}
