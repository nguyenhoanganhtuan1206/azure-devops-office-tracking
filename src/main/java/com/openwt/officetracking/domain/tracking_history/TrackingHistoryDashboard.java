package com.openwt.officetracking.domain.tracking_history;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

@Getter
@Setter
@With
@Builder
public class TrackingHistoryDashboard {

    private int lateCount;

    private int earlyCount;

    private int earlyAndLateCount;

    private int inTimeCount;

    private int totalCount;
}