package com.openwt.officetracking.persistent.device_history;

import com.openwt.officetracking.domain.device_history.DeviceHistory;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class DeviceHistoryEntityMapper {

    public static DeviceHistoryEntity toDeviceHistoryEntity(final DeviceHistory deviceHistory) {
        return DeviceHistoryEntity.builder()
                .id(deviceHistory.getId())
                .deviceId(deviceHistory.getDeviceId())
                .assignUserId(deviceHistory.getAssignUserId())
                .deviceStatus(deviceHistory.getDeviceStatus())
                .deviceCondition(deviceHistory.getCondition())
                .note(deviceHistory.getNote())
                .latestUpdateTime(deviceHistory.getLatestUpdateTime())
                .previousUpdateTime(deviceHistory.getPreviousUpdateTime())
                .build();
    }

    public static DeviceHistory toDeviceHistory(final DeviceHistoryEntity deviceHistoryEntity) {
        return DeviceHistory.builder()
                .id(deviceHistoryEntity.getId())
                .deviceId(deviceHistoryEntity.getDeviceId())
                .assignUserId(deviceHistoryEntity.getAssignUserId())
                .deviceStatus(deviceHistoryEntity.getDeviceStatus())
                .condition(deviceHistoryEntity.getDeviceCondition())
                .note(deviceHistoryEntity.getNote())
                .previousUpdateTime(deviceHistoryEntity.getPreviousUpdateTime())
                .latestUpdateTime(deviceHistoryEntity.getLatestUpdateTime())
                .build();
    }

    public static List<DeviceHistory> toDeviceHistories(final List<DeviceHistoryEntity> deviceHistoryEntities) {
        return deviceHistoryEntities.stream()
                .map(DeviceHistoryEntityMapper::toDeviceHistory)
                .toList();
    }
}