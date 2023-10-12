package com.openwt.officetracking.api.tracking_history;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

@Getter
@Setter
@Builder
@With
public class OfficeAttendanceSummaryDTO {

    private int currentEmployees;

    private int checkinEmployees;

    private int checkoutEmployees;
}