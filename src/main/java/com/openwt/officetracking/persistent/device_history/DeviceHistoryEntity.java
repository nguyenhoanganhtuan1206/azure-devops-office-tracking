package com.openwt.officetracking.persistent.device_history;

import com.openwt.officetracking.domain.device_status.DeviceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Table(name = "device_histories")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DeviceHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID deviceId;

    private UUID assignUserId;

    @Enumerated(EnumType.STRING)
    private DeviceStatus deviceStatus;

    private String deviceCondition;

    private String note;

    private Instant latestUpdateTime;

    private Instant previousUpdateTime;
}
