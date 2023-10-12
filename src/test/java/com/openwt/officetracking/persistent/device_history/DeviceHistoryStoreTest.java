package com.openwt.officetracking.persistent.device_history;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.openwt.officetracking.fake.DeviceHistoryFakes.buildDeviceHistoryEntities;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceHistoryStoreTest {

    @Mock
    private DeviceHistoryRepository deviceHistoryRepository;

    @InjectMocks
    private DeviceHistoryStore deviceHistoryStore;

    @Test
    void shouldFindByDeviceId_OK() {
        final var deviceId = randomUUID();
        final var deviceHistories = buildDeviceHistoryEntities();

        when(deviceHistoryRepository.findByDeviceId(deviceId))
                .thenReturn(deviceHistories);

        final var actual = deviceHistoryStore.findByDeviceId(deviceId);

        assertEquals(deviceHistories.size(), actual.size());

        verify(deviceHistoryRepository).findByDeviceId(deviceId);
    }
}
