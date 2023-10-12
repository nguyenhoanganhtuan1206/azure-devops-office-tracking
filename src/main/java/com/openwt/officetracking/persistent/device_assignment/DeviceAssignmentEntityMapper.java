package com.openwt.officetracking.persistent.device_assignment;

import com.openwt.officetracking.domain.device_assignment.DeviceAssignment;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class DeviceAssignmentEntityMapper {

    public static DeviceAssignment toDeviceAssignment(final DeviceAssignmentEntity deviceAssignmentEntity) {
        return DeviceAssignment.builder()
                .id(deviceAssignmentEntity.getId())
                .userId(deviceAssignmentEntity.getUserId())
                .deviceId(deviceAssignmentEntity.getDeviceId())
                .fromTimeAt(deviceAssignmentEntity.getFromTimeAt())
                .toTimeAt(deviceAssignmentEntity.getToTimeAt())
                .deviceStatus(deviceAssignmentEntity.getDeviceStatus())
                .build();
    }

    public static List<DeviceAssignment> toDeviceAssignments(final List<DeviceAssignmentEntity> deviceAssignmentEntities) {
        return deviceAssignmentEntities.stream()
                .map(DeviceAssignmentEntityMapper::toDeviceAssignment)
                .toList();
    }

    public static DeviceAssignmentEntity toDeviceAssignmentEntity(final DeviceAssignment deviceAssignment) {
        return DeviceAssignmentEntity.builder()
                .id(deviceAssignment.getId())
                .userId(deviceAssignment.getUserId())
                .deviceId(deviceAssignment.getDeviceId())
                .fromTimeAt(deviceAssignment.getFromTimeAt())
                .toTimeAt(deviceAssignment.getToTimeAt())
                .deviceStatus(deviceAssignment.getDeviceStatus())
                .build();
    }
}