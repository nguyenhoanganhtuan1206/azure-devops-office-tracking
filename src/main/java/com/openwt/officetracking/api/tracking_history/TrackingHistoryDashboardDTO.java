package com.openwt.officetracking.api.tracking_history;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TrackingHistoryDashboardDTO {

    private int lateCount;

    private int earlyCount;

    private int earlyAndLateCount;

    private int inTimeCount;

    private int totalCount;
}
