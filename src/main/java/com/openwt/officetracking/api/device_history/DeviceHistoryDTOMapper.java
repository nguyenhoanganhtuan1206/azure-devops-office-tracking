package com.openwt.officetracking.api.device_history;

import com.openwt.officetracking.domain.device_history.DeviceHistory;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class DeviceHistoryDTOMapper {

    public static DeviceHistoryDTO toDeviceHistoryDTO(final DeviceHistory deviceHistory) {
        return DeviceHistoryDTO.builder()
                .id(deviceHistory.getId())
                .deviceId(deviceHistory.getDeviceId())
                .assignUserId(deviceHistory.getAssignUserId())
                .deviceStatus(deviceHistory.getDeviceStatus())
                .condition(deviceHistory.getCondition())
                .note(deviceHistory.getNote())
                .latestUpdateTime(deviceHistory.getLatestUpdateTime())
                .previousUpdateTime(deviceHistory.getPreviousUpdateTime())
                .build();
    }

    public static List<DeviceHistoryDTO> toDeviceHistoryDTOs(final List<DeviceHistory> deviceHistories) {
        return deviceHistories.stream()
                .map(DeviceHistoryDTOMapper::toDeviceHistoryDTO)
                .toList();
    }
}
