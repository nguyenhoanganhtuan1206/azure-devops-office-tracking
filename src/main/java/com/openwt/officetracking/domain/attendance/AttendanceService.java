package com.openwt.officetracking.domain.attendance;

import com.openwt.officetracking.domain.attendance_status.AttendanceStatus;
import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.door.Door;
import com.openwt.officetracking.domain.door.DoorService;
import com.openwt.officetracking.domain.fcm.FirebaseMessagingService;
import com.openwt.officetracking.domain.fcm.NotificationMessage;
import com.openwt.officetracking.domain.office.Office;
import com.openwt.officetracking.domain.office.OfficeService;
import com.openwt.officetracking.domain.tracking_history.AttendanceGeofencingRequest;
import com.openwt.officetracking.domain.tracking_history.TrackingHistory;
import com.openwt.officetracking.domain.user.User;
import com.openwt.officetracking.domain.user.UserService;
import com.openwt.officetracking.domain.user_mobile.UserMobile;
import com.openwt.officetracking.domain.user_mobile.UserMobileService;
import com.openwt.officetracking.persistent.tracking_history.TrackingHistoryStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.openwt.officetracking.domain.attendance.TrackingHistoryError.supplyHistoryTrackingNotFound;
import static com.openwt.officetracking.error.CommonError.supplyAccessDeniedError;
import static java.lang.String.format;
import static java.time.Instant.now;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final TrackingHistoryStore trackingHistoryStore;

    private final DoorService doorService;

    private final UserService userService;

    private final OfficeService officeService;

    private final UserMobileService userMobileService;

    private final AuthsProvider authsProvider;

    private final FirebaseMessagingService firebaseMessagingService;

    private static final String NOTIFICATION_TITLE = "Office Attendance Management";

    public TrackingHistory findTodayHistoryForUser(final UUID userId) {
        return trackingHistoryStore.findTodayHistoryForUser(userId)
                .orElseThrow(supplyHistoryTrackingNotFound("User Id", userId));
    }

    public TrackingHistory updateHistoryWithGeofenceCheckinCheckout(final AttendanceGeofencingRequest attendanceGeofencingRequest) {
        final Office currentOffice = officeService.findById(attendanceGeofencingRequest.getOfficeId());
        final User currentUser = userService.findById(authsProvider.getCurrentUserId());
        final TrackingHistory currentTrackingHistory = createNewTrackingHistoryIfNotAvailable(currentUser.getId());

        return trackingHistoryStore.save(currentTrackingHistory
                .withOfficeId(currentOffice.getId())
                .withEntryExitStatus(attendanceGeofencingRequest.getActivityType()));
    }

    public TrackingHistory updateHistoryWhenCheckInOutWithDoor(final AttendanceBeaconRequest attendanceBeaconRequest) {
        final User currentUser = userService.findById(authsProvider.getCurrentUserId());
        final Door door = doorService.findByMajorAndMinor(attendanceBeaconRequest.getMajor(), attendanceBeaconRequest.getMinor());
        final Office currentOffice = officeService.findById(door.getOfficeId());

        final TrackingHistory currentTrackingHistory = createNewTrackingHistoryIfNotAvailable(currentUser.getId());

        if (currentTrackingHistory.getCheckinAt() == null) {
            firebaseMessagingService.sendNotification(buildNotificationMessageForCheckIn());

            return trackingHistoryStore.save(updatePropertiesHistoryTracking(currentTrackingHistory, currentOffice));
        }

        return trackingHistoryStore.save(currentTrackingHistory
                .withCheckoutBeaconAt(now()));
    }

    public TrackingHistory updateHistoryWhenCheckInOutWithManually(final AttendanceBiometryRequest attendanceBiometryRequest) {
        final UserMobile currentUserMobile = userMobileService.findByBiometryToken(attendanceBiometryRequest.getBiometryToken());
        final User currentUser = userService.findByCurrentUserId();
        final Office currentOffice = officeService.findById(attendanceBiometryRequest.getOfficeId());
        verifyIfUserMobileNotBelongsToUser(currentUser, currentUserMobile);

        final TrackingHistory currentTrackingHistory = createNewTrackingHistoryIfNotAvailable(currentUser.getId());

        if (currentTrackingHistory.getCheckinAt() == null) {
            firebaseMessagingService.sendNotification(buildNotificationMessageForCheckIn());
            return trackingHistoryStore.save(updatePropertiesHistoryTracking(currentTrackingHistory, currentOffice))
                    .withAttendanceStatus(AttendanceStatus.CHECK_IN);
        }

        firebaseMessagingService.sendNotification(buildNotificationMessageForCheckOut());
        return trackingHistoryStore.save(currentTrackingHistory
                        .withCheckoutAt(now()))
                .withAttendanceStatus(AttendanceStatus.CHECK_OUT);
    }

    private TrackingHistory createNewTrackingHistoryIfNotAvailable(final UUID userId) {
        return trackingHistoryStore.findTodayHistoryForUser(userId)
                .orElseGet(() -> {
                    final TrackingHistory currentTrackingHistory = TrackingHistory.builder()
                            .userId(userId)
                            .trackedDate(Instant.now())
                            .build();

                    return trackingHistoryStore.save(currentTrackingHistory);
                });
    }

    private NotificationMessage buildNotificationMessageForCheckIn() {
        return NotificationMessage.builder()
                .title(NOTIFICATION_TITLE)
                .body(format("Your Check-in time today is %s", getCurrentTimeInGMTPlus7()))
                .build();
    }

    private NotificationMessage buildNotificationMessageForCheckOut() {
        return NotificationMessage.builder()
                .title(NOTIFICATION_TITLE)
                .body(format("Your Check-out time today is %s", getCurrentTimeInGMTPlus7()))
                .build();
    }

    private String getCurrentTimeInGMTPlus7() {
        final ZoneId gmtPlus7Zone = ZoneId.of("GMT+7");
        final LocalDateTime currentTimeInGMTPlus7 = LocalDateTime.now(gmtPlus7Zone);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return currentTimeInGMTPlus7.format(formatter);
    }

    private void verifyIfUserMobileNotBelongsToUser(final User currentUser, final UserMobile currentUserMobile) {
        if (!currentUserMobile.getUserId().equals(currentUser.getId())) {
            throw supplyAccessDeniedError().get();
        }
    }

    private TrackingHistory updatePropertiesHistoryTracking(final TrackingHistory currentTrackingHistory,
                                                            final Office currentOffice) {
        return currentTrackingHistory
                .withCheckinAt(now())
                .withOfficeId(currentOffice.getId());
    }
}