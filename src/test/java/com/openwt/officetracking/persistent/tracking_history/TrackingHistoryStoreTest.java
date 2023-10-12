package com.openwt.officetracking.persistent.tracking_history;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

import static com.openwt.officetracking.fake.TrackingHistoryFakes.*;
import static com.openwt.officetracking.fake.UserFakes.buildUser;
import static com.openwt.officetracking.persistent.tracking_history.TrackingHistoryEntityMapper.toTrackingHistoryEntity;
import static java.time.Instant.now;
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
    void shouldSave_OK() {
        final var historyTracking = buildTrackingHistory();

        when(trackingHistoryRepository.save(any()))
                .thenReturn(toTrackingHistoryEntity(historyTracking));

        final var actual = trackingHistoryStore.save(historyTracking);

        assertEquals(historyTracking.getId(), actual.getId());
        assertEquals(historyTracking.getTrackedDate(), actual.getTrackedDate());
        assertEquals(historyTracking.getUserId(), actual.getUserId());
        assertEquals(historyTracking.getOfficeId(), actual.getOfficeId());
        assertEquals(historyTracking.getCheckoutAt(), actual.getCheckoutAt());
        assertEquals(historyTracking.getCheckinAt(), actual.getCheckinAt());

        verify(trackingHistoryRepository).save(any());
    }

    @Test
    void shouldFindTodayHistoryForUser_OK() {
        final var historyTracking = buildTrackingHistoryEntity();

        when(trackingHistoryRepository.findTodayHistoryForUser(historyTracking.getUserId()))
                .thenReturn(Optional.of(historyTracking));

        final var actual = trackingHistoryStore.findTodayHistoryForUser(historyTracking.getUserId()).get();

        assertEquals(historyTracking.getId(), actual.getId());
        assertEquals(historyTracking.getTrackedDate(), actual.getTrackedDate());
        assertEquals(historyTracking.getUserId(), actual.getUserId());
        assertEquals(historyTracking.getOfficeId(), actual.getOfficeId());
        assertEquals(historyTracking.getCheckoutAt(), actual.getCheckoutAt());
        assertEquals(historyTracking.getCheckinAt(), actual.getCheckinAt());

        verify(trackingHistoryRepository).findTodayHistoryForUser(historyTracking.getUserId());
    }

    @Test
    void shouldFind5LatestRecord_OK() {
        final var user = buildUser();
        final var pageable = PageRequest.of(0, 2);
        final var historyTrackingEntities = buildTrackingHistoryEntities();

        when(trackingHistoryRepository.findLatestTrackingHistories(user.getId(), pageable)).thenReturn(historyTrackingEntities);

        final var actual = trackingHistoryStore.findLatestTrackingHistories(user.getId(), pageable);

        assertEquals(historyTrackingEntities.size(), actual.size());

        verify(trackingHistoryRepository).findLatestTrackingHistories(user.getId(), pageable);
    }

    @Test
    void shouldFindTrackingHistoriesInRange_OK() {
        final var user = buildUser();
        final var historyTrackingEntities = buildTrackingHistoryEntities();
        final var startDate = LocalDate.now()
                .with(TemporalAdjusters.firstDayOfMonth())
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC);
        final var endDate = LocalDate.now()
                .plusDays(1)
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC);

        when(trackingHistoryRepository.findByTrackedDateInRange(user.getId(), startDate, endDate)).thenReturn(historyTrackingEntities);

        final var actual = trackingHistoryStore.findTrackingHistoriesInRange(user.getId(), startDate, endDate);

        assertEquals(historyTrackingEntities.size(), actual.size());

        verify(trackingHistoryRepository).findByTrackedDateInRange(user.getId(), startDate, endDate);
    }

    @Test
    void shouldFindByTrackedDateBetween_OK() {
        final var startOfDay = now();
        final var endOfDay = startOfDay.plus(1, ChronoUnit.DAYS);
        final var trackingHistories = buildTrackingHistoryEntities();

        when(trackingHistoryRepository.findByTrackedDateBetween(startOfDay, endOfDay)).thenReturn(trackingHistories);

        final var actual = trackingHistoryStore.findByTrackedDateBetween(startOfDay, endOfDay);

        assertEquals(trackingHistories.size(), actual.size());

        verify(trackingHistoryRepository).findByTrackedDateBetween(startOfDay, endOfDay);
    }
}