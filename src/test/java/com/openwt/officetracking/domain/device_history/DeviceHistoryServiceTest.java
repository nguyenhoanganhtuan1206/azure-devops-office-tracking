package com.openwt.officetracking.domain.device_history;

import com.openwt.officetracking.persistent.device_history.DeviceHistoryStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.DeviceFakes.buildDevice;
import static com.openwt.officetracking.fake.DeviceHistoryFakes.buildDeviceHistory;
import static com.openwt.officetracking.fake.DeviceHistoryFakes.buildDeviceHistories;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceHistoryServiceTest {

    @Mock
    private DeviceHistoryStore deviceHistoryStore;

    @InjectMocks
    private DeviceHistoryService deviceHistoryService;

    @Test
    public void shouldCreateWithDeviceOnAssignment_OK() {
        final var device = buildDevice();
        final var latestHistory = buildDeviceHistory().withPreviousUpdateTime(null);

        when(deviceHistoryStore.findFirstByDeviceIdWithLatestUpdateTime(device.getId()))
                .thenReturn(Optional.of(latestHistory));
        when(deviceHistoryStore.save(latestHistory)).thenReturn(latestHistory);

        deviceHistoryService.createWithDeviceOnAssignment(device);

        verify(deviceHistoryStore).findFirstByDeviceIdWithLatestUpdateTime(device.getId());
        verify(deviceHistoryStore).save(latestHistory);
    }

    @Test
    void shouldFindByDeviceId_OK() {
        final var deviceId = randomUUID();
        final var deviceHistories = buildDeviceHistories();

        when(deviceHistoryStore.findByDeviceId(deviceId))
                .thenReturn(deviceHistories);

        final var actual = deviceHistoryService.findByDeviceId(deviceId);

        assertEquals(deviceHistories.size(), actual.size());
        assertEquals(deviceHistories, actual);

        verify(deviceHistoryStore).findByDeviceId(deviceId);
    }
}
