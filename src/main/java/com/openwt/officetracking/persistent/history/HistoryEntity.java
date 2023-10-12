package com.openwt.officetracking.persistent.history;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Table(name = "history")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Instant checkinTime;

    private Instant checkoutTime;

    private String note;

    private UUID userId;

    private UUID bookingId;
}
