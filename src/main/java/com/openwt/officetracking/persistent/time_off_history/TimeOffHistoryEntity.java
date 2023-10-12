package com.openwt.officetracking.persistent.time_off_history;

import com.openwt.officetracking.domain.time_off_request_status.TimeOffRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Table(name = "time_off_histories")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TimeOffHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID timeOffRequestId;

    private UUID userId;

    @Enumerated(EnumType.STRING)
    private TimeOffRequestStatus modificationType;

    private Instant modifiedAt;
}
