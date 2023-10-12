package com.openwt.officetracking.persistent.device;

import com.openwt.officetracking.domain.device_status.DeviceStatus;
import com.openwt.officetracking.domain.request_status.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Table(name = "devices")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID assignUserId;

    private UUID requestUserId;

    private UUID deviceRequestId;

    private UUID modelId;

    private String detail;

    @Enumerated(EnumType.STRING)
    private DeviceStatus deviceStatus;

    private String serialNumber;

    private Instant purchaseAt;

    private Instant warrantyEndAt;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    private boolean isRequested;

    private String note;

    private String reason;

    private Instant requestedAt;

    private Instant acceptedAt;

    private Instant rejectedAt;

    private Instant completedAt;

    private Instant createdAt;

    private Instant lastModifiedAt;
}
