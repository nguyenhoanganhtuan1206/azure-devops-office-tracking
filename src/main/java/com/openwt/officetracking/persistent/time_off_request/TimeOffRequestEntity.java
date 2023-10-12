package com.openwt.officetracking.persistent.time_off_request;

import com.openwt.officetracking.domain.time_off_request_status.TimeOffRequestStatus;
import com.openwt.officetracking.domain.time_off_request_type.TimeOffRequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Table(name = "time_off_requests")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TimeOffRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID userId;

    private UUID typeId;

    private Instant startAt;

    private Instant endAt;

    @Enumerated(EnumType.STRING)
    private TimeOffRequestType requestType;

    private String description;

    @Enumerated(EnumType.STRING)
    private TimeOffRequestStatus status;

    private String attachFileUrl1;

    private String attachFileUrl2;

    private Instant requestedAt;

    private Instant acceptedAt;

    private Instant rejectedAt;

    private Instant cancelledAt;
}
