package com.openwt.officetracking.persistent.history_tracking;

import com.openwt.officetracking.persistent.tracking_history.TrackingHistoryRepository;
import com.openwt.officetracking.persistent.tracking_history.TrackingHistoryStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static com.openwt.officetracking.fake.TrackingHistoryFakes.buildTrackingHistoryEntities;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrackingHistoryStoreTest {

    @Mock
    private TrackingHistoryRepository trackingHistoryRepository;

    @InjectMocks
    private TrackingHistoryStore trackingHistoryStore;

    @Test
    void shouldFindByTrackedDateBetween_OK() {
        final var historyTrackingEntities = buildTrackingHistoryEntities();
        final var startOfDay = Instant.now();

        when(trackingHistoryRepository.findTrackingHistoriesByTrackedDate(any(Instant.class)))
                .thenReturn(historyTrackingEntities);

        final var actual = trackingHistoryStore.findTrackingHistoriesByTrackedDate(startOfDay);

        assertEquals(historyTrackingEntities.size(), actual.size());

        verify(trackingHistoryRepository).findTrackingHistoriesByTrackedDate(any(Instant.class));
    }
}