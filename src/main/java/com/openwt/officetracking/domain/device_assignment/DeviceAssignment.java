package com.openwt.officetracking.domain.device_assignment;

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
public class DeviceAssignment {

    private UUID id;

    private UUID userId;

    private UUID deviceId;

    private Instant fromTimeAt;

    private Instant toTimeAt;

    private DeviceStatus deviceStatus;
}
