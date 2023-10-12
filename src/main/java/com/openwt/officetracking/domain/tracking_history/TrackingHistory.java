package com.openwt.officetracking.domain.tracking_history;

import com.openwt.officetracking.domain.attendance_status.AttendanceStatus;
import com.openwt.officetracking.domain.entry_exit_status.EntryExitStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@With
public class TrackingHistory {

    private UUID id;

    private UUID officeId;

    private UUID userId;

    private Instant trackedDate;

    private Instant checkinAt;

    private Instant checkoutAt;

    private Instant checkoutBeaconAt;

    private Integer workingHours;

    private Integer lateTime;

    private Integer earlyTime;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;

    @Enumerated(EnumType.STRING)
    private EntryExitStatus entryExitStatus;
}