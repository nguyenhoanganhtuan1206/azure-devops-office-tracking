package com.openwt.officetracking.persistent.tracking_history;

import com.openwt.officetracking.domain.entry_exit_status.EntryExitStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Table(name = "tracking_histories")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TrackingHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID officeId;

    private UUID userId;

    private Instant trackedDate;

    private Instant checkinAt;

    private Instant checkoutAt;

    private Instant checkoutBeaconAt;

    @Enumerated(EnumType.STRING)
    private EntryExitStatus entryExitStatus;
}