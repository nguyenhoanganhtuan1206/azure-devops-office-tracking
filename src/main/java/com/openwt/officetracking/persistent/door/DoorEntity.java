package com.openwt.officetracking.persistent.door;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Table(name = "doors")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DoorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private int major;

    private int minor;

    private String note;

    private UUID officeId;

    private Instant createdAt;
}