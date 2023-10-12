package com.openwt.officetracking.api.tracking_history;

import com.openwt.officetracking.domain.attendance_status.AttendanceStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.time.Instant;

@Getter
@Setter
@Builder
@With
public class TrackingHistoryStatus {

    private AttendanceStatus attendanceStatus;

    private Instant currentCheckInTime;

    private Instant currentCheckOutTime;
}