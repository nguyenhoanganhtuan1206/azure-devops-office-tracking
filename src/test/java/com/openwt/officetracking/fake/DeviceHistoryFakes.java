package com.openwt.officetracking.fake;

import com.openwt.officetracking.domain.device_history.DeviceHistory;
import com.openwt.officetracking.domain.device_status.DeviceStatus;
import com.openwt.officetracking.persistent.device_history.DeviceHistoryEntity;
import lombok.experimental.UtilityClass;

import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class DeviceHistoryFakes {

    public static DeviceHistory buildDeviceHistory() {
        return DeviceHistory.builder()
                .id(randomUUID())
                .deviceId(randomUUID())
                .assignUserId(randomUUID())
                .deviceStatus(DeviceStatus.ASSIGNED)
                .condition(randomAlphabetic(9))
                .note(randomAlphabetic(9))
                .latestUpdateTime(now())
                .previousUpdateTime(now().minus(10, ChronoUnit.MINUTES))
                .build();
    }

    public static List<DeviceHistory> buildDeviceHistories() {
        return buildList(DeviceHistoryFakes::buildDeviceHistory);
    }

    public static DeviceHistoryEntity buildDeviceHistoryEntity() {
        return DeviceHistoryEntity.builder()
                .id(randomUUID())
                .deviceId(randomUUID())
                .assignUserId(randomUUID())
                .deviceStatus(DeviceStatus.ASSIGNED)
                .deviceCondition(randomAlphabetic(9))
                .note(randomAlphabetic(9))
                .latestUpdateTime(now())
                .previousUpdateTime(now().minus(10, ChronoUnit.MINUTES))
                .build();
    }

    public static List<DeviceHistoryEntity> buildDeviceHistoryEntities() {
        return buildList(DeviceHistoryFakes::buildDeviceHistoryEntity);
    }
}
