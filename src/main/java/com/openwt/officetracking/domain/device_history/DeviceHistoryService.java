package com.openwt.officetracking.domain.device_history;

import com.openwt.officetracking.domain.device.Device;
import com.openwt.officetracking.domain.device.DeviceRequest;
import com.openwt.officetracking.domain.device_status.DeviceStatus;
import com.openwt.officetracking.domain.user.UserService;
import com.openwt.officetracking.persistent.device_history.DeviceHistoryStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.domain.device_history.DeviceHistoryError.supplyDeviceHistoryNotFound;
import static java.time.Instant.now;

@Service
@RequiredArgsConstructor
public class DeviceHistoryService {

    private final DeviceHistoryStore deviceHistoryStore;

    private final UserService userService;

    public DeviceHistory findLatestDeviceHistory(final UUID deviceId) {
        return deviceHistoryStore.findFirstByDeviceIdWithLatestUpdateTime(deviceId)
                .orElseThrow(supplyDeviceHistoryNotFound("id", deviceId));
    }

    public void createWithDeviceOnAssignment(final Device device) {
        final DeviceHistory latestHistory = findLatestDeviceHistory(device.getId());

        updateDeviceHistoryWithPreviousUpdateTimeByDeviceHistory(latestHistory);

        final DeviceHistory newHistory = DeviceHistory.builder()
                .deviceId(device.getId())
                .assignUserId(device.getAssignUserId())
                .deviceStatus(device.getDeviceStatus())
                .note(device.getRequestNote())
                .condition(latestHistory.getCondition())
                .latestUpdateTime(null)
                .previousUpdateTime(now())
                .build();

        deviceHistoryStore.save(newHistory);
    }

    private void updateDeviceHistoryWithPreviousUpdateTimeByDeviceHistory(final DeviceHistory deviceHistory) {
        if (deviceHistory != null) {
            updateDeviceHistoryWithPreviousUpdateTime(deviceHistory);
        }
    }

    public List<DeviceHistory> findByDeviceId(final UUID deviceId) {
        return deviceHistoryStore.findByDeviceId(deviceId);
    }

    public DeviceHistory createWithNewDevice(final Device device, final DeviceRequest deviceRequest) {
        updateDeviceHistoryWithLatestUpdateTimeByDevice(device);
        return deviceHistoryStore.save(buildDeviceHistoryByDeviceWithDeviceRequest(device, deviceRequest));
    }

    private DeviceHistory buildDeviceHistoryByDeviceWithDeviceRequest(final Device device, final DeviceRequest deviceRequest) {
        if (deviceRequest.getDeviceStatus() != DeviceStatus.ASSIGNED) {
            verifyIfUserAssignedNotAvailable(deviceRequest.getAssignUserId());
        }

        return DeviceHistory.builder()
                .deviceId(device.getId())
                .assignUserId(deviceRequest.getAssignUserId())
                .deviceStatus(deviceRequest.getDeviceStatus())
                .note(deviceRequest.getNote())
                .condition(deviceRequest.getCondition())
                .latestUpdateTime(null)
                .previousUpdateTime(now())
                .build();
    }

    private void verifyIfUserAssignedNotAvailable(final UUID assignUser) {
        if (assignUser != null) {
            userService.findById(assignUser);
        }
    }

    private void updateDeviceHistoryWithLatestUpdateTimeByDevice(final Device currentDevice) {
        final Optional<DeviceHistory> deviceHistoryOptional = deviceHistoryStore.findFirstByDeviceIdWithLatestUpdateTime(currentDevice.getId());

        deviceHistoryOptional.ifPresent(this::updateDeviceHistoryWithPreviousUpdateTime);
    }

    private void updateDeviceHistoryWithPreviousUpdateTime(final DeviceHistory latestHistory) {
        latestHistory.setPreviousUpdateTime(latestHistory.getPreviousUpdateTime());
        latestHistory.setLatestUpdateTime(now());
        deviceHistoryStore.save(latestHistory);
    }
}
