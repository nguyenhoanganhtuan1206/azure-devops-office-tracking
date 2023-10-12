package com.openwt.officetracking.domain.attendance;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AttendanceBeaconRequest {

    private int major;

    private int minor;
}
