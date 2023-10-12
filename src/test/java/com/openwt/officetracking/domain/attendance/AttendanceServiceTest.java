package com.openwt.officetracking.domain.attendance;

import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.door.DoorService;
import com.openwt.officetracking.domain.entry_exit_status.EntryExitStatus;
import com.openwt.officetracking.domain.fcm.FirebaseMessagingService;
import com.openwt.officetracking.domain.fcm.NotificationMessage;
import com.openwt.officetracking.domain.office.OfficeService;
import com.openwt.officetracking.domain.tracking_history.AttendanceGeofencingRequest;
import com.openwt.officetracking.domain.tracking_history.TrackingHistory;
import com.openwt.officetracking.domain.user.UserService;
import com.openwt.officetracking.domain.user_mobile.UserMobileService;
import com.openwt.officetracking.persistent.tracking_history.TrackingHistoryStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.DoorFakes.buildDoor;
import static com.openwt.officetracking.fake.OfficeFakes.buildOffice;
import static com.openwt.officetracking.fake.TrackingHistoryFakes.buildTrackingHistory;
import static com.openwt.officetracking.fake.UserFakes.buildUser;
import static com.openwt.officetracking.fake.UserMobileFakes.buildUserMobile;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock
    private TrackingHistoryStore trackingHistoryStore;

    @Mock
    private UserService userService;

    @Mock
    private OfficeService officeService;

    @Mock
    private AuthsProvider authsProvider;

    @InjectMocks
    private AttendanceService attendanceService;

    @Mock
    private DoorService doorService;

    @Mock
    private UserMobileService userMobileService;

    @Mock
    private FirebaseMessagingService firebaseMessagingService;

    @Test
    void shouldFindTodayHistoryForUser_OK() {
        final var userId = randomUUID();
        final var historyTracking = buildTrackingHistory();

        when(trackingHistoryStore.findTodayHistoryForUser(userId))
                .thenReturn(Optional.of(historyTracking));

        final var actual = attendanceService.findTodayHistoryForUser(userId);

        assertEquals(historyTracking.getId(), actual.getId());
        assertEquals(historyTracking.getUserId(), actual.getUserId());
        assertEquals(historyTracking.getOfficeId(), actual.getOfficeId());
        assertEquals(historyTracking.getTrackedDate(), actual.getTrackedDate());
        assertEquals(historyTracking.getCheckinAt(), actual.getCheckinAt());
        assertEquals(historyTracking.getCheckoutAt(), actual.getCheckoutAt());
        assertEquals(historyTracking.getWorkingHours(), actual.getWorkingHours());
        assertEquals(historyTracking.getLateTime(), actual.getLateTime());
        assertEquals(historyTracking.getEarlyTime(), actual.getEarlyTime());

        verify(trackingHistoryStore).findTodayHistoryForUser(userId);
    }

    @Test
    void shouldUpdateHistoryWithGeofenceCheckinCheckout_OK() {
        final var office = buildOffice();
        final var user = buildUser();
        final var historyTracking = buildTrackingHistory()
                .withOfficeId(office.getId());
        final var attendanceRequest = AttendanceGeofencingRequest.builder()
                .activityType(EntryExitStatus.ENTER)
                .officeId(office.getId())
                .build();

        when(authsProvider.getCurrentUserId())
                .thenReturn(user.getId());
        when(officeService.findById(office.getId()))
                .thenReturn(office);
        when(userService.findById(user.getId()))
                .thenReturn(user);
        when(trackingHistoryStore.findTodayHistoryForUser(user.getId()))
                .thenReturn(Optional.of(historyTracking));
        when(trackingHistoryStore.save(any(TrackingHistory.class)))
                .thenReturn(historyTracking);

        final var actual = attendanceService.updateHistoryWithGeofenceCheckinCheckout(attendanceRequest);

        assertEquals(office.getId(), actual.getOfficeId());

        verify(authsProvider).getCurrentUserId();
        verify(officeService).findById(office.getId());
        verify(userService).findById(user.getId());
        verify(trackingHistoryStore).findTodayHistoryForUser(user.getId());
        verify(trackingHistoryStore).save(any(TrackingHistory.class));
    }

    @Test
    void shouldUpdateHistoryWhenCheckInOutWithDoor_OK() {
        final var office = buildOffice();
        final var user = buildUser();
        final var door = buildDoor()
                .withOfficeId(office.getId());
        final var historyTracking = buildTrackingHistory()
                .withUserId(user.getId())
                .withOfficeId(office.getId());
        final var attendanceBeaconRequest = AttendanceBeaconRequest.builder()
                .major(door.getMajor())
                .minor(door.getMinor())
                .build();

        when(authsProvider.getCurrentUserId())
                .thenReturn(user.getId());
        when(userService.findById(user.getId()))
                .thenReturn(user);
        when(doorService.findByMajorAndMinor(door.getMajor(), door.getMinor()))
                .thenReturn(door);
        when(trackingHistoryStore.findTodayHistoryForUser(user.getId()))
                .thenReturn(Optional.of(historyTracking));
        when(officeService.findById(office.getId()))
                .thenReturn(office);
        when(trackingHistoryStore.save(any(TrackingHistory.class)))
                .thenReturn(historyTracking);

        final var actual = attendanceService.updateHistoryWhenCheckInOutWithDoor(attendanceBeaconRequest);

        assertEquals(historyTracking.getTrackedDate(), actual.getTrackedDate());
        assertEquals(historyTracking.getCheckinAt(), actual.getCheckinAt());
        assertEquals(historyTracking.getOfficeId(), actual.getOfficeId());

        verify(authsProvider).getCurrentUserId();
        verify(userService).findById(user.getId());
        verify(doorService).findByMajorAndMinor(attendanceBeaconRequest.getMajor(), attendanceBeaconRequest.getMinor());
        verify(officeService).findById(office.getId());
        verify(trackingHistoryStore).findTodayHistoryForUser(user.getId());
        verify(trackingHistoryStore).save(any(TrackingHistory.class));
    }

    @Test
    void shouldUpdateHistoryWhenCheckInOutWithManually_OK() {
        final var office = buildOffice();
        final var user = buildUser();
        final var userMobile = buildUserMobile()
                .withUserId(user.getId());
        final var attendanceRequest = AttendanceBiometryRequest.builder()
                .biometryToken(userMobile.getBiometryToken())
                .officeId(office.getId())
                .build();
        final var historyTracking = buildTrackingHistory()
                .withUserId(user.getId());

        when(userMobileService.findByBiometryToken(userMobile.getBiometryToken()))
                .thenReturn(userMobile);
        when(userService.findByCurrentUserId())
                .thenReturn(user);
        when(officeService.findById(office.getId()))
                .thenReturn(office);
        when(trackingHistoryStore.findTodayHistoryForUser(user.getId()))
                .thenReturn(Optional.of(historyTracking));
        doNothing().when(firebaseMessagingService)
                .sendNotification(any(NotificationMessage.class));
        when(trackingHistoryStore.save(any(TrackingHistory.class)))
                .thenReturn(historyTracking);

        final var actual = attendanceService.updateHistoryWhenCheckInOutWithManually(attendanceRequest);

        assertEquals(historyTracking.getTrackedDate(), actual.getTrackedDate());
        assertEquals(historyTracking.getCheckinAt(), actual.getCheckinAt());
        assertEquals(historyTracking.getOfficeId(), actual.getOfficeId());

        verify(userMobileService).findByBiometryToken(userMobile.getBiometryToken());
        verify(userService).findByCurrentUserId();
        verify(officeService).findById(office.getId());
        verify(firebaseMessagingService).sendNotification(any(NotificationMessage.class));
        verify(trackingHistoryStore).findTodayHistoryForUser(user.getId());
    }
}