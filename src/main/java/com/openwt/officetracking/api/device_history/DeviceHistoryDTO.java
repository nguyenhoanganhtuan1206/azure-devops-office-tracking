package com.openwt.officetracking.api.device_history;

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
public class DeviceHistoryDTO {

    private UUID id;

    private UUID deviceId;

    private UUID assignUserId;

    private DeviceStatus deviceStatus;

    private String condition;

    private String note;

    private Instant latestUpdateTime;

    private Instant previousUpdateTime;
}
