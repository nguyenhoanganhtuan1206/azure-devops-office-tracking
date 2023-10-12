package com.openwt.officetracking.persistent.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Table(name = "booking")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Instant startAt;

    private Instant endAt;

    private UUID userId;
}
