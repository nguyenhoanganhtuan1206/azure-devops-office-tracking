package com.openwt.officetracking.persistent.device_history;

import com.openwt.officetracking.domain.device_history.DeviceHistory;
import com.openwt.officetracking.domain.device_status.DeviceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.persistent.device_history.DeviceHistoryEntityMapper.*;

@Repository
@RequiredArgsConstructor
public class DeviceHistoryStore {

    private final DeviceHistoryRepository deviceHistoryRepository;

    public List<DeviceHistory> findByDeviceId(final UUID deviceId) {
        return toDeviceHistories(deviceHistoryRepository.findByDeviceId(deviceId));
    }

    public DeviceHistory save(final DeviceHistory deviceHistory) {
        return toDeviceHistory(deviceHistoryRepository.save(toDeviceHistoryEntity(deviceHistory)));
    }

    public Optional<DeviceHistory> findFirstByDeviceIdWithLatestUpdateTime(final UUID deviceId) {
        return deviceHistoryRepository.findFirstByDeviceIdAndLatestUpdateTimeIsNullOrderByPreviousUpdateTimeDesc(deviceId)
                .map(DeviceHistoryEntityMapper::toDeviceHistory);
    }

    public Optional<DeviceHistory> findDevicePreviousConfirmHistory(final UUID deviceId) {
        return deviceHistoryRepository.findLatestUtilizedHistory(deviceId)
                .map(DeviceHistoryEntityMapper::toDeviceHistory);
    }

    public Optional<DeviceHistory> findDevicePreviousHistory(final UUID deviceId, final DeviceStatus deviceStatus) {
        return deviceHistoryRepository.findLatestDeviceStatusDeviceWithNullUpdateTimeHistory(deviceId, deviceStatus)
                .map(DeviceHistoryEntityMapper::toDeviceHistory);
    }
}
