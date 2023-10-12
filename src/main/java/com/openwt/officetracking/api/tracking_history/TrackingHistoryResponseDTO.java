package com.openwt.officetracking.api.tracking_history;

import com.openwt.officetracking.domain.attendance_status.AttendanceStatus;
import com.openwt.officetracking.domain.entry_exit_status.EntryExitStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@With
public class TrackingHistoryResponseDTO {

    private UUID id;

    private UUID officeId;

    private UUID userId;

    private Instant trackedDate;

    private Instant checkinAt;

    private Instant checkoutAt;

    private Integer workingHours;

    private Integer lateTime;

    private Integer earlyTime;

    private AttendanceStatus attendanceStatus;

    private EntryExitStatus entryExitStatus;
}