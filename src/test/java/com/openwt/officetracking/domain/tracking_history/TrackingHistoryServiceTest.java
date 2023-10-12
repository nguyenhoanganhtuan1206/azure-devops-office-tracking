package com.openwt.officetracking.domain.tracking_history;

import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.position.PositionService;
import com.openwt.officetracking.domain.role.Role;
import com.openwt.officetracking.domain.user.UserService;
import com.openwt.officetracking.persistent.tracking_history.TrackingHistoryStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.fake.PositionFakes.buildPosition;
import static com.openwt.officetracking.fake.TrackingHistoryFakes.*;
import static com.openwt.officetracking.fake.UserFakes.buildUser;
import static com.openwt.officetracking.fake.UserFakes.buildUsers;
import static java.time.Instant.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrackingHistoryServiceTest {

    @Mock
    private TrackingHistoryStore trackingHistoryStore;

    @Mock
    private UserService userService;

    @Mock
    private PositionService positionService;

    @Mock
    private AuthsProvider authsProvider;

    @InjectMocks
    private TrackingHistoryService trackingHistoryService;

    @Test
    void shouldFindTrackingHistoriesByTrackedDate_OK() {
        final var position = buildPosition();
        final var user = buildUser()
                .withPositionId(position.getId());
        final var historiesTracking = buildTrackingHistory()
                .withUserId(user.getId());
        final var startOfDay = Instant.now()
                .atZone(ZoneOffset.UTC)
                .toLocalDate()
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();

        when(positionService.findAll())
                .thenReturn(List.of(position));
        when(userService.findAll())
                .thenReturn(List.of(user));
        when(trackingHistoryStore.findTrackingHistoriesByTrackedDate(any(Instant.class)))
                .thenReturn(List.of(historiesTracking));

        final var actual = trackingHistoryService.findFilteredTrackingHistories(startOfDay);

        assertNotNull(actual);

        verify(trackingHistoryStore).findTrackingHistoriesByTrackedDate(any(Instant.class));
    }

    @Test
    void shouldCreateHistoriesTracking_OK() {
        final var users = buildUsers();
        users.forEach(user -> user.setRole(Role.USER));
        final var historyTracking = buildTrackingHistory().withUserId(users.get(0).getId());

        when(userService.findAll()).thenReturn(users);
        when(trackingHistoryStore.save(argThat(historyArg -> historyArg.getUserId() == historyTracking.getUserId())))
                .thenReturn(historyTracking);

        trackingHistoryService.createTrackingHistories();

        verify(userService).findAll();
        verify(trackingHistoryStore).save(argThat(historyArg -> historyArg.getUserId()
                == historyTracking.getUserId()));
    }

    @Test
    void shouldFindByCurrentUser_InRangeTime_OK() {
        final var user = buildUser();
        final var pageable = PageRequest.of(0, 2);
        final var startDate = LocalDate.now();
        final var endDate = startDate.plusDays(1);
        final var start = startDate.atStartOfDay()
                .atOffset(ZoneOffset.ofHours(7))
                .toInstant();
        final var end = endDate.plusDays(1)
                .atStartOfDay()
                .minusSeconds(1)
                .atOffset(ZoneOffset.ofHours(7))
                .toInstant();
        final var trackingHistories = buildTrackingHistories();

        when(authsProvider.getCurrentUserId()).thenReturn(user.getId());
        when(trackingHistoryStore.findTrackingHistoriesInRange(user.getId(), start, end)).thenReturn(trackingHistories);

        final var actual = trackingHistoryService.findByCurrentUser(startDate, endDate, pageable);

        assertEquals(trackingHistories.get(0).getId(), actual.get(0).getId());
        assertEquals(trackingHistories.get(0).getTrackedDate(), actual.get(0).getTrackedDate());
        assertEquals(trackingHistories.get(0).getCheckinAt(), actual.get(0).getCheckinAt());
        assertEquals(trackingHistories.get(0).getCheckoutAt(), actual.get(0).getCheckoutAt());

        verify(authsProvider).getCurrentUserId();
        verify(trackingHistoryStore).findTrackingHistoriesInRange(user.getId(), start, end);
    }

    @Test
    void shouldFindByCurrentUser_Get5LatestRecords_OK() {
        final var user = buildUser();
        final var pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "trackedDate"));
        final var trackingHistories = buildTrackingHistories();

        when(authsProvider.getCurrentUserId()).thenReturn(user.getId());
        when(trackingHistoryStore.findLatestTrackingHistories(user.getId(), pageable)).thenReturn(trackingHistories);

        final var actual = trackingHistoryService.findByCurrentUser(null, null, pageable);

        assertEquals(trackingHistories.get(0).getId(), actual.get(0).getId());
        assertEquals(trackingHistories.get(0).getTrackedDate(), actual.get(0).getTrackedDate());
        assertEquals(trackingHistories.get(0).getCheckinAt(), actual.get(0).getCheckinAt());
        assertEquals(trackingHistories.get(0).getCheckoutAt(), actual.get(0).getCheckoutAt());

        verify(authsProvider).getCurrentUserId();
        verify(trackingHistoryStore).findLatestTrackingHistories(user.getId(), pageable);
    }

    @Test
    void shouldFindByCurrentUser_DefaultTime_OK() {
        final var user = buildUser();
        final var pageable = PageRequest.of(0, 5);
        final var firstDayOfMonth = LocalDate.now()
                .with(TemporalAdjusters.firstDayOfMonth())
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC);
        final var nextDay = LocalDate.now()
                .plusDays(1)
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC);
        final var trackingHistories = buildTrackingHistories();

        when(authsProvider.getCurrentUserId()).thenReturn(user.getId());
        when(trackingHistoryStore.findTrackingHistoriesInRange(user.getId(), firstDayOfMonth, nextDay)).thenReturn(trackingHistories);

        final var actual = trackingHistoryService.findByCurrentUser(null, null, pageable);

        assertEquals(trackingHistories.get(0).getId(), actual.get(0).getId());
        assertEquals(trackingHistories.get(0).getTrackedDate(), actual.get(0).getTrackedDate());
        assertEquals(trackingHistories.get(0).getCheckinAt(), actual.get(0).getCheckinAt());
        assertEquals(trackingHistories.get(0).getCheckoutAt(), actual.get(0).getCheckoutAt());

        verify(authsProvider).getCurrentUserId();
        verify(trackingHistoryStore).findTrackingHistoriesInRange(user.getId(), firstDayOfMonth, nextDay);
    }

    @Test
    void shouldCalculateAttendanceSummary_OK() {
        final var analysisDate = now()
                .minus(1, ChronoUnit.DAYS)
                .atZone(ZoneOffset.UTC)
                .toLocalDate()
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();
        final var historiesTracking = buildTrackingHistories();
        final var dashboard = buildTrackingHistoryDashboard();

        when(trackingHistoryStore.findTrackingHistoriesByTrackedDate(analysisDate)).thenReturn(historiesTracking);

        final var actual = trackingHistoryService.calculateAttendanceSummary(analysisDate);

        assertEquals(dashboard.getTotalCount(), actual.getTotalCount());

        verify(trackingHistoryStore).findTrackingHistoriesByTrackedDate(analysisDate);
    }

    @Test
    void shouldFindCurrentStatusByUserId_OK() {
        final var trackingHistory = buildTrackingHistory();

        when(authsProvider.getCurrentUserId())
                .thenReturn(trackingHistory.getUserId());
        when(trackingHistoryStore.findTodayHistoryForUser(any(UUID.class)))
                .thenReturn(Optional.of(trackingHistory));

        trackingHistoryService.findCurrentStatusByUserId();

        verify(authsProvider).getCurrentUserId();
        verify(trackingHistoryStore).findTodayHistoryForUser(any(UUID.class));
    }
}