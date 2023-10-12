package com.openwt.officetracking.api.attendance;

import com.openwt.officetracking.domain.entry_exit_status.EntryExitStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class AttendanceGeofencingRequestDTO {

    private UUID officeId;

    private EntryExitStatus activityType;
}
