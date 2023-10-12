package com.openwt.officetracking.api.tracking_history;

import com.openwt.officetracking.api.user.UserDetailDTO;
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
public class TrackingHistoryDetailDTO {

    private UUID id;

    private UserDetailDTO user;

    private Instant trackedDate;

    private Instant checkinAt;

    private Instant checkoutAt;

    private Integer workingHours;
}
