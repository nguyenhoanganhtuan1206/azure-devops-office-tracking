package com.openwt.officetracking.domain.tracking_history;

import com.openwt.officetracking.api.tracking_history.TrackingHistoryStatus;
import com.openwt.officetracking.domain.attendance_status.AttendanceStatus;
import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.entry_exit_status.EntryExitStatus;
import com.openwt.officetracking.domain.position.Position;
import com.openwt.officetracking.domain.position.PositionService;
import com.openwt.officetracking.domain.role.Role;
import com.openwt.officetracking.domain.user.User;
import com.openwt.officetracking.domain.user.UserService;
import com.openwt.officetracking.persistent.tracking_history.TrackingHistoryStore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.domain.position.PositionError.supplyPositionIdNotFound;
import static com.openwt.officetracking.domain.tracking_history.TrackingHistoryError.supplyTrackingHistoryNotFound;
import static com.openwt.officetracking.domain.user.UserError.supplyUserNotFound;
import static com.openwt.officetracking.domain.user_mobile.UserMobileService.buildUserDetail;
import static java.time.LocalTime.now;

@Service
@RequiredArgsConstructor
public class TrackingHistoryService {

    private final TrackingHistoryStore trackingHistoryStore;

    private final UserService userService;

    private final PositionService positionService;

    private final AuthsProvider authsProvider;

    private static final String CREATE_HISTORY_CRON_EXPRESSION = "${app.schedule.create-tracking-histories}";

    private static final String UPDATE_HISTORY_CRON_EXPRESSION = "${app.schedule.update-tracking-histories}";

    private static final String TIME_ZONE = "${app.schedule.time-zone}";

    private static final Duration REST_DURATION = Duration.ofHours(1).plusMinutes(30);

    private static final Duration REQUIRED_WORKING_HOURS = Duration.ofHours(8).plusMinutes(0);

    private static final LocalTime TARGET_TIME = LocalTime.of(21, 0);

    private static final ZoneOffset LOCAL_ZONE_OFFSET = ZoneOffset.ofHours(7);

    private static final Instant TARGET_MOBILE_DISPLAY_TIME = LocalDateTime.now()
            .withHour(19)
            .withMinute(30)
            .withSecond(0)
            .toInstant(LOCAL_ZONE_OFFSET);

    public TrackingHistory findTodayHistoryForUser(final UUID userId) {
        return trackingHistoryStore.findTodayHistoryForUser(userId)
                .orElseThrow(supplyTrackingHistoryNotFound("User Id", userId));
    }

    public TrackingHistoryStatus findCurrentStatusByUserId() {
        final TrackingHistory currentTrackingHistory = findTodayHistoryForUser(authsProvider.getCurrentUserId());

        if (Instant.now().isAfter(TARGET_MOBILE_DISPLAY_TIME)) {
            return TrackingHistoryStatus.builder()
                    .currentCheckInTime(currentTrackingHistory.getCheckinAt())
                    .currentCheckOutTime(getLatestCheckedOutTime(currentTrackingHistory.getCheckoutAt(), currentTrackingHistory.getCheckoutBeaconAt()))
                    .attendanceStatus(AttendanceStatus.CHECK_OUT)
                    .build();
        }

        if (currentTrackingHistory.getCheckinAt() == null) {
            return TrackingHistoryStatus.builder()
                    .attendanceStatus(AttendanceStatus.NOT_YET)
                    .build();
        }

        return TrackingHistoryStatus.builder()
                .currentCheckInTime(currentTrackingHistory.getCheckinAt())
                .currentCheckOutTime(currentTrackingHistory.getCheckoutAt() != null ? currentTrackingHistory.getCheckoutAt() : null)
                .attendanceStatus(currentTrackingHistory.getCheckoutAt() != null ? AttendanceStatus.CHECK_OUT : AttendanceStatus.CHECK_IN)
                .build();
    }

    public List<TrackingHistoryDetails> findAll() {
        final List<User> users = userService.findAll();
        final List<TrackingHistory> trackingHistories = trackingHistoryStore.findAll();
        final List<Position> positions = positionService.findAll();

        return trackingHistories.stream()
                .map(trackingHistory -> updateTrackingHistoryWithProperties(trackingHistory, trackingHistory.getTrackedDate(), users, positions))
                .toList();
    }

    public List<TrackingHistoryDetails> findFilteredTrackingHistories(final Instant trackedDate) {
        if (trackedDate == null) {
            return findAll();
        }

        final Instant startOfDay = toStartOfDay(trackedDate);
        final List<User> users = userService.findAll();
        final List<Position> positions = positionService.findAll();
        final List<TrackingHistory> trackingHistories = trackingHistoryStore.findTrackingHistoriesByTrackedDate(startOfDay);

        return trackingHistories.stream()
                .map(trackingHistory -> updateTrackingHistoryWithProperties(trackingHistory, trackedDate, users, positions))
                .toList();
    }

    public OfficeAttendanceSummary analyzeAttendanceDataByDate(final Instant analysisDate) {
        final Instant startOfDay = toStartOfDay(analysisDate);

        final List<TrackingHistory> trackingHistories = trackingHistoryStore.findTrackingHistoriesByTrackedDate(startOfDay);

        final int checkedInEmployeesCount = (int) trackingHistories.stream()
                .filter(trackingHistory -> trackingHistory.getCheckinAt() != null)
                .count();

        final int currentEmployeesCount = (int) trackingHistories.stream()
                .filter(trackingHistory -> trackingHistory.getCheckinAt() != null && trackingHistory.getEntryExitStatus() == EntryExitStatus.ENTER)
                .count();

        final int checkedOutEmployeesCount = (int) trackingHistories.stream()
                .filter(trackingHistory -> trackingHistory.getCheckoutAt() != null)
                .count();

        return OfficeAttendanceSummary.builder()
                .currentEmployees(currentEmployeesCount)
                .checkedInEmployees(checkedInEmployeesCount)
                .checkedOutEmployees(checkedOutEmployeesCount)
                .build();
    }

    @Scheduled(cron = CREATE_HISTORY_CRON_EXPRESSION, zone = TIME_ZONE)
    public void createTrackingHistories() {
        final List<User> employees = userService.findAll().stream()
                .filter(user -> user.getRole() == Role.USER)
                .toList();

        employees.forEach(this::createHistory);
    }

    @Scheduled(cron = UPDATE_HISTORY_CRON_EXPRESSION, zone = TIME_ZONE)
    public void updateTrackingHistories() {
        final List<TrackingHistory> trackingHistories = trackingHistoryStore.findTrackingHistoriesByTrackedDate(Instant.now());

        trackingHistories.forEach(this::updateAndSaveTrackingHistory);
    }

    public List<TrackingHistory> findByCurrentUser(final LocalDate startDate, final LocalDate endDate, final Pageable pageable) {
        final UUID currentUserId = authsProvider.getCurrentUserId();

        if (startDate != null && endDate != null) {
            final Instant start = startDate.atStartOfDay()
                    .atOffset(LOCAL_ZONE_OFFSET)
                    .toInstant();
            final Instant end = endDate.plusDays(1)
                    .atStartOfDay()
                    .minusSeconds(1)
                    .atOffset(LOCAL_ZONE_OFFSET)
                    .toInstant();

            return updateCheckoutForLastTrackedDate(trackingHistoryStore.findTrackingHistoriesInRange(currentUserId, start, end));
        }

        if (pageable.getSort().getOrderFor("trackedDate") != null) {
            return updateCheckoutForLastTrackedDate(trackingHistoryStore.findLatestTrackingHistories(currentUserId, pageable));
        }

        final Instant firstDayOfMonth = LocalDate.now()
                .with(TemporalAdjusters.firstDayOfMonth())
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC);

        final Instant nextDay = LocalDate.now()
                .plusDays(1)
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC);

        return updateCheckoutForLastTrackedDate(trackingHistoryStore.findTrackingHistoriesInRange(currentUserId, firstDayOfMonth, nextDay));
    }

    private List<TrackingHistory> updateCheckoutForLastTrackedDate(List<TrackingHistory> trackingHistories) {
        final Instant lastTrackedDate = trackingHistories.stream()
                .map(TrackingHistory::getTrackedDate)
                .max(Instant::compareTo)
                .orElse(Instant.MIN);

        if (!lastTrackedDate.equals(Instant.MIN)) {
            final Instant currentDate = LocalDate.now()
                    .atStartOfDay()
                    .toInstant(LOCAL_ZONE_OFFSET);

            trackingHistories = trackingHistories.stream()
                    .peek(history -> {
                        if (toStartOfDay(history.getTrackedDate()).equals(toStartOfDay(lastTrackedDate))) {
                            if (toStartOfDay(history.getTrackedDate()).equals(toStartOfDay(currentDate)) &&
                                    Instant.now().isBefore(TARGET_MOBILE_DISPLAY_TIME)) {
                                history.setCheckoutAt(null);
                            }
                        }
                    })
                    .toList();
        }

        return trackingHistories;
    }

    public TrackingHistoryDashboard calculateAttendanceSummary(final Instant analysisDate) {
        final Instant startOfDay = toStartOfDay(analysisDate);
        int lateCount = 0;
        int earlyCount = 0;
        int inTimeCount = 0;
        int earlyAndLateCount = 0;

        final List<TrackingHistory> trackingHistories = trackingHistoryStore.findTrackingHistoriesByTrackedDate(startOfDay).stream()
                .filter(trackingHistory -> trackingHistory.getCheckinAt() != null)
                .toList();

        for (final TrackingHistory trackingHistory : trackingHistories) {
            final int lateTime = durationToMinutes(calculateLateTime(trackingHistory.getCheckinAt()));
            final int earlyTime = durationToMinutes(calculateEarlyTime(trackingHistory.getCheckinAt(), trackingHistory.getCheckoutAt()));

            if (earlyTime != 0) {
                if (lateTime == 0) {
                    earlyCount++;
                    continue;
                }
                earlyAndLateCount++;
                continue;
            }
            if (lateTime == 0) {
                inTimeCount++;
                continue;
            }
            lateCount++;
        }

        return TrackingHistoryDashboard.builder()
                .lateCount(lateCount)
                .earlyCount(earlyCount)
                .inTimeCount(inTimeCount)
                .earlyAndLateCount(earlyAndLateCount)
                .totalCount(trackingHistories.size())
                .build();
    }

    private void updateAndSaveTrackingHistory(final TrackingHistory trackingHistory) {
        final Instant finalTime = getLatestCheckedOutTime(trackingHistory.getCheckoutAt(), trackingHistory.getCheckoutBeaconAt());

        trackingHistory.setCheckoutAt(finalTime);

        trackingHistoryStore.save(trackingHistory);
    }

    private Instant getLatestCheckedOutTime(final Instant checkoutAt, final Instant checkoutBeaconAt) {
        return (checkoutAt == null && checkoutBeaconAt == null)
                ? null
                : (checkoutAt == null || checkoutBeaconAt == null)
                ? (checkoutAt == null ? checkoutBeaconAt : checkoutAt)
                : (checkoutAt.isAfter(checkoutBeaconAt) ? checkoutAt : checkoutBeaconAt);
    }

    private void createHistory(final User employee) {
        final TrackingHistory trackingHistory = TrackingHistory.builder()
                .userId(employee.getId())
                .trackedDate(Instant.now())
                .build();

        trackingHistoryStore.save(trackingHistory);
    }

    private TrackingHistoryDetails updateTrackingHistoryWithProperties(final TrackingHistory trackingHistory,
                                                                       final Instant trackedDate,
                                                                       final List<User> users,
                                                                       final List<Position> positions) {
        final TrackingHistoryDetails currentTrackingHistory = buildTrackingHistory(trackingHistory, users, positions);

        if (trackedDate.isBefore(Instant.now()) || now().isAfter(TARGET_TIME)) {
            final Instant checkinTime = currentTrackingHistory.getCheckinAt();
            final Instant checkoutTime = currentTrackingHistory.getCheckoutAt();
            final Duration workingDuration = calculateWorkingHours(checkinTime, checkoutTime);

            return currentTrackingHistory
                    .withWorkingHours(durationToMinutes(workingDuration));
        }

        return currentTrackingHistory;
    }

    private Duration calculateWorkingHours(final Instant checkInTime, final Instant checkOutTime) {
        if (checkInTime == null || checkOutTime == null) {
            return Duration.ZERO;
        }

        return Duration.between(checkInTime, checkOutTime).minus(REST_DURATION);
    }

    private Duration calculateEarlyTime(final Instant checkInTime, final Instant checkOutTime) {
        final Duration workingDuration = calculateWorkingHours(checkInTime, checkOutTime);

        if (workingDuration != Duration.ZERO && workingDuration.compareTo(REQUIRED_WORKING_HOURS) < 0) {
            return REQUIRED_WORKING_HOURS.minus(workingDuration);
        }

        return Duration.ZERO;
    }

    private static Duration calculateLateTime(final Instant checkInTime) {
        final Instant startWorkingTime = checkInTime.atZone(ZoneOffset.UTC)
                .toLocalDate()
                .atTime(LocalTime.of(9, 0))
                .atOffset(LOCAL_ZONE_OFFSET)
                .toInstant();

        if (checkInTime.isAfter(startWorkingTime)) {
            return Duration.between(startWorkingTime, checkInTime);
        }

        return Duration.ZERO;
    }

    private Integer durationToMinutes(final Duration duration) {
        return Math.toIntExact(duration.toMinutes());
    }

    private TrackingHistoryDetails buildTrackingHistory(final TrackingHistory trackingHistory, final List<User> users, final List<Position> positions) {
        final User currentUser = findUserById(users, trackingHistory.getUserId());
        final Position currentPosition = findPositionById(positions, currentUser.getPositionId());
        final Duration lateTimeDuration = trackingHistory.getCheckinAt() != null
                ? calculateLateTime(trackingHistory.getCheckinAt())
                : Duration.ZERO;

        return TrackingHistoryDetails.builder()
                .id(trackingHistory.getId())
                .user(buildUserDetail(currentUser, currentPosition))
                .trackedDate(trackingHistory.getTrackedDate())
                .checkinAt(trackingHistory.getCheckinAt())
                .checkoutAt(trackingHistory.getCheckoutAt())
                .lateTime(durationToMinutes(lateTimeDuration))
                .build();
    }

    private User findUserById(final List<User> users, final UUID userId) {
        return users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(supplyUserNotFound("id", userId));
    }

    private Position findPositionById(final List<Position> positions, final UUID positionId) {
        return positions.stream()
                .filter(position -> position.getId().equals(positionId))
                .findFirst()
                .orElseThrow(supplyPositionIdNotFound(positionId));
    }

    private static Instant toStartOfDay(final Instant analyzeDate) {
        return analyzeDate.atZone(ZoneOffset.UTC)
                .toLocalDate()
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();
    }
}