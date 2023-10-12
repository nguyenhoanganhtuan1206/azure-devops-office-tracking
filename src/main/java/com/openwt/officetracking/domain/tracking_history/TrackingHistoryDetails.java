package com.openwt.officetracking.domain.tracking_history;

import com.openwt.officetracking.domain.user.UserDetail;
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
public class TrackingHistoryDetails {


    private UUID id;

    private UserDetail user;

    private Instant trackedDate;

    private Instant checkinAt;

    private Instant checkoutAt;

    private Integer workingHours;

    private Integer lateTime;

    private Integer earlyTime;

    private Integer total;
}
