package com.openwt.officetracking.api.tracking_history;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTrackingHistoryDTO {

    private UUID id;

    private Instant trackedDate;

    private Instant checkinAt;

    private Instant checkoutAt;
}
