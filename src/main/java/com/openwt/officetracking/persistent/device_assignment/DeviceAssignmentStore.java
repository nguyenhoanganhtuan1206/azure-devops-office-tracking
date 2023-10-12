package com.openwt.officetracking.persistent.device_assignment;

import com.openwt.officetracking.domain.device_assignment.DeviceAssignment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.persistent.device_assignment.DeviceAssignmentEntityMapper.*;

@Repository
@RequiredArgsConstructor
public class DeviceAssignmentStore {

    private final DeviceAssignmentRepository deviceAssignmentRepository;

    public Optional<DeviceAssignment> findByUserIdAndDeviceId(final UUID userId, final UUID deviceId) {
        return deviceAssignmentRepository.findByUserIdAndDeviceId(userId, deviceId)
                .map(DeviceAssignmentEntityMapper::toDeviceAssignment);
    }

    public List<DeviceAssignment> findByDeviceId(final UUID deviceId) {
        return toDeviceAssignments(deviceAssignmentRepository.findByDeviceId(deviceId));
    }

    public DeviceAssignment save(final DeviceAssignment deviceAssignment) {
        return toDeviceAssignment(deviceAssignmentRepository.save(toDeviceAssignmentEntity(deviceAssignment)));
    }
}