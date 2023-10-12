package com.openwt.officetracking.persistent.time_off_detail;

import com.openwt.officetracking.domain.day_duration.DayDuration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Table(name = "time_off_details")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TimeOffDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID timeOffRequestId;

    private Instant dayOff;

    @Enumerated(EnumType.STRING)
    private DayDuration duration;
}
