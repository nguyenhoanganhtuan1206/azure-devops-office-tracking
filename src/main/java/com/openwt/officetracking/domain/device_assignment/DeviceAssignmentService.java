package com.openwt.officetracking.domain.device_assignment;

import com.openwt.officetracking.domain.device_history.DeviceHistory;
import com.openwt.officetracking.domain.device_status.DeviceStatus;
import com.openwt.officetracking.persistent.device_assignment.DeviceAssignmentStore;
import com.openwt.officetracking.persistent.device_history.DeviceHistoryStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.error.CommonError.supplyNotFoundError;
import static java.time.Instant.now;

@Service
@RequiredArgsConstructor
public class DeviceAssignmentService {

    private final DeviceAssignmentStore deviceAssignmentStore;

    private final DeviceHistoryStore deviceHistoryStore;

    public DeviceAssignment save(final DeviceAssignment deviceAssignment) {
        return deviceAssignmentStore.save(deviceAssignment);
    }

    public DeviceAssignment findByUserIdAndDeviceId(final UUID userId, final UUID deviceId) {
        return deviceAssignmentStore.findByUserIdAndDeviceId(userId, deviceId)
                .orElseThrow(supplyNotFoundError("Device Assignment is not existed!"));
    }

    public void createByAssignUserIdAndDeviceId(final UUID currentUserId,
                                                final UUID userId,
                                                final UUID deviceId,
                                                final DeviceStatus requestStatus) {
        final List<DeviceAssignment> deviceAssignments = deviceAssignmentStore.findByDeviceId(deviceId);

        if (deviceAssignments.isEmpty()) {
            final DeviceAssignment newAssignment = buildNewDeviceAssignment(deviceId, userId);
            deviceAssignmentStore.save(newAssignment);
            return;
        }

        updateDeviceAssignmentsForUsers(currentUserId, userId, deviceId, requestStatus);
    }

    private void updateDeviceAssignmentsForUsers(final UUID currentUserId,
                                                 final UUID userRequestId,
                                                 final UUID deviceId,
                                                 final DeviceStatus requestStatus) {
        final Optional<DeviceAssignment> deviceAssignmentCurrentUser = deviceAssignmentStore.findByUserIdAndDeviceId(currentUserId, deviceId);
        final Optional<DeviceAssignment> deviceAssignmentRequestUser = deviceAssignmentStore.findByUserIdAndDeviceId(userRequestId, deviceId);

        deviceAssignmentCurrentUser.ifPresent(assignment -> {
            assignment.setDeviceStatus(requestStatus != DeviceStatus.AVAILABLE ? updateRequestStatusBasedOnHistory(requestStatus, assignment) : DeviceStatus.RETURNED);
            assignment.setToTimeAt(now());
            deviceAssignmentStore.save(assignment);
        });

        if (userRequestId != null && deviceAssignmentRequestUser.isEmpty()) {
            final DeviceAssignment newAssignment = buildNewDeviceAssignment(deviceId, userRequestId);
            deviceAssignmentStore.save(newAssignment);
            return;
        }

        deviceAssignmentRequestUser.ifPresent(assignment -> {
            assignment.setFromTimeAt(now());
            assignment.setToTimeAt(null);
            assignment.setDeviceStatus(DeviceStatus.ASSIGNED);
            deviceAssignmentStore.save(assignment);
        });
    }

    private DeviceAssignment buildNewDeviceAssignment(final UUID deviceId, final UUID userId) {
        return DeviceAssignment.builder()
                .deviceId(deviceId)
                .userId(userId)
                .fromTimeAt(now())
                .toTimeAt(null)
                .deviceStatus(DeviceStatus.ASSIGNED)
                .build();
    }

    private DeviceStatus updateRequestStatusBasedOnHistory(final DeviceStatus requestStatus, final DeviceAssignment deviceAssignment) {
        final Optional<DeviceHistory> devicePreviousConfirmHistory = deviceHistoryStore.findDevicePreviousConfirmHistory(deviceAssignment.getDeviceId());
        final Optional<DeviceHistory> deviceUtilizedHistory = deviceHistoryStore.findDevicePreviousHistory(deviceAssignment.getDeviceId(), DeviceStatus.UTILIZED);

        if (devicePreviousConfirmHistory.isPresent() && deviceAssignment.getDeviceStatus() == DeviceStatus.REPAIRING && requestStatus == DeviceStatus.SCRAPPED) {
            return DeviceStatus.RETURNED;
        }

        if (deviceUtilizedHistory.isEmpty() &&
                (requestStatus == DeviceStatus.REPAIRING ||
                        requestStatus == DeviceStatus.LOST ||
                        requestStatus == DeviceStatus.ASSIGNED)) {
            return DeviceStatus.RETURNED;
        }

        return requestStatus;
    }
}