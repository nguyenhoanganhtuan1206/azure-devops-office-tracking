package com.openwt.officetracking.fake;

import com.openwt.officetracking.domain.device_assignment.DeviceAssignment;
import com.openwt.officetracking.domain.device_status.DeviceStatus;
import com.openwt.officetracking.persistent.device_assignment.DeviceAssignmentEntity;
import lombok.experimental.UtilityClass;

import java.time.Instant;

import static java.util.UUID.randomUUID;

@UtilityClass
public class DeviceAssignmentFakes {

    public static DeviceAssignment buildDeviceAssignment() {
        return DeviceAssignment.builder()
                .id(randomUUID())
                .deviceId(randomUUID())
                .userId(randomUUID())
                .fromTimeAt(Instant.now())
                .toTimeAt(Instant.now())
                .deviceStatus(DeviceStatus.ASSIGNED)
                .build();
    }

    public static DeviceAssignmentEntity buildDeviceAssignmentEntity() {
        return DeviceAssignmentEntity.builder()
                .id(randomUUID())
                .deviceId(randomUUID())
                .userId(randomUUID())
                .fromTimeAt(Instant.now())
                .toTimeAt(Instant.now())
                .deviceStatus(DeviceStatus.ASSIGNED)
                .build();
    }
}
