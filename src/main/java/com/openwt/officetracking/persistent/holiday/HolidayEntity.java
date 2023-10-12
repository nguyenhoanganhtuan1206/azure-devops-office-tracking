package com.openwt.officetracking.persistent.holiday;

import com.openwt.officetracking.domain.holiday_type.HolidayType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Table(name = "holidays")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HolidayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private Instant date;

    @Enumerated(EnumType.STRING)
    private HolidayType type;
}
