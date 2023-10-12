package com.openwt.officetracking.domain.tracking_history;

import com.openwt.officetracking.domain.entry_exit_status.EntryExitStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class AttendanceGeofencingRequest {

    private UUID officeId;

    private EntryExitStatus activityType;
}
