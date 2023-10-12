package com.openwt.officetracking.persistent.time_off_reason;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "time_off_reasons")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TimeOffReasonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID type_id;

    private String name;
}
