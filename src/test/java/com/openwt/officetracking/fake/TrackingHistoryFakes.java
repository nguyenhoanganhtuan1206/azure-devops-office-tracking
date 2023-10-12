package com.openwt.officetracking.fake;

import com.openwt.officetracking.api.tracking_history.TrackingHistoryDetailDTO;
import com.openwt.officetracking.api.tracking_history.TrackingHistoryResponseDTO;
import com.openwt.officetracking.domain.attendance_status.AttendanceStatus;
import com.openwt.officetracking.domain.entry_exit_status.EntryExitStatus;
import com.openwt.officetracking.domain.tracking_history.OfficeAttendanceSummary;
import com.openwt.officetracking.domain.tracking_history.TrackingHistory;
import com.openwt.officetracking.domain.tracking_history.TrackingHistoryDashboard;
import com.openwt.officetracking.domain.tracking_history.TrackingHistoryDetails;
import com.openwt.officetracking.persistent.tracking_history.TrackingHistoryEntity;
import lombok.experimental.UtilityClass;

import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.*;
import static com.openwt.officetracking.fake.UserFakes.buildUserDetail;
import static com.openwt.officetracking.fake.UserFakes.buildUserDetailDTO;
import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

@UtilityClass
public class TrackingHistoryFakes {

    public static TrackingHistory buildTrackingHistory() {
        final var checkinAt = randomInstant();

        return TrackingHistory.builder()
                .id(randomUUID())
                .userId(randomUUID())
                .officeId(randomUUID())
                .trackedDate(randomInstant())
                .checkinAt(checkinAt)
                .checkoutAt(checkinAt.plusSeconds(100))
                .workingHours(100)
                .earlyTime(100)
                .lateTime(100)
                .entryExitStatus(EntryExitStatus.ENTER)
                .build();
    }

    public static List<TrackingHistory> buildTrackingHistories() {
        return buildList(TrackingHistoryFakes::buildTrackingHistory);
    }

    public static TrackingHistoryEntity buildTrackingHistoryEntity() {
        final var checkinAt = randomInstant();

        return TrackingHistoryEntity.builder()
                .id(randomUUID())
                .userId(randomUUID())
                .officeId(randomUUID())
                .trackedDate(randomInstant())
                .checkinAt(checkinAt)
                .checkoutAt(checkinAt.plusSeconds(100))
                .entryExitStatus(EntryExitStatus.ENTER)
                .build();
    }

    public static OfficeAttendanceSummary buildOfficeAttendanceSummary() {
        return OfficeAttendanceSummary.builder()
                .currentEmployees(randomInteger(50))
                .checkedInEmployees(randomInteger(50))
                .checkedOutEmployees(randomInteger(50))
                .build();
    }

    public static List<TrackingHistoryEntity> buildTrackingHistoryEntities() {
        return buildList(TrackingHistoryFakes::buildTrackingHistoryEntity);
    }

    public static TrackingHistoryDetails buildTrackingHistoryDetail() {
        return TrackingHistoryDetails.builder()
                .id(randomUUID())
                .user(buildUserDetail())
                .checkinAt(now())
                .checkoutAt(now().plus(8, ChronoUnit.HOURS))
                .workingHours(randomInteger(50))
                .earlyTime(randomInteger(50))
                .lateTime(randomInteger(50))
                .build();
    }

    public static List<TrackingHistoryDetails> buildTrackingHistoryDetails() {
        return buildList(TrackingHistoryFakes::buildTrackingHistoryDetail);
    }

    public static TrackingHistoryDashboard buildTrackingHistoryDashboard() {
        return TrackingHistoryDashboard.builder()
                .earlyAndLateCount(0)
                .earlyCount(4)
                .lateCount(0)
                .inTimeCount(0)
                .totalCount(4)
                .build();
    }

    public static TrackingHistoryResponseDTO buildTrackingHistoryResponseDTO() {
        final var checkinAt = randomInstant();

        return TrackingHistoryResponseDTO.builder()
                .id(randomUUID())
                .userId(randomUUID())
                .officeId(randomUUID())
                .trackedDate(randomInstant())
                .checkinAt(checkinAt)
                .checkoutAt(checkinAt.plusSeconds(100))
                .workingHours(100)
                .earlyTime(100)
                .lateTime(100)
                .attendanceStatus(AttendanceStatus.CHECK_IN)
                .entryExitStatus(EntryExitStatus.ENTER)
                .build();
    }

    public static TrackingHistoryDetailDTO buildTrackingHistoryDetailDTO() {
        return TrackingHistoryDetailDTO.builder()
                .id(randomUUID())
                .user(buildUserDetailDTO())
                .checkinAt(now())
                .checkoutAt(now().plus(8, ChronoUnit.HOURS))
                .workingHours(randomInteger(50))
                .build();
    }
}