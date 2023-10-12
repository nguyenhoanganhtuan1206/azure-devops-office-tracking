package com.openwt.officetracking.persistent.device_assignment;

import com.openwt.officetracking.domain.device_status.DeviceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Table(name = "device_assignments")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DeviceAssignmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID userId;

    private UUID deviceId;

    private Instant fromTimeAt;

    private Instant toTimeAt;

    @Enumerated(EnumType.STRING)
    private DeviceStatus deviceStatus;
}
