package com.openwt.officetracking.persistent.time_off_type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "time_off_types")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TimeOffTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
}
