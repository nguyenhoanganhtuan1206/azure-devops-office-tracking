package com.openwt.officetracking.api.attendance;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AttendanceBeaconRequestDTO {

    private int major;

    private int minor;
}
