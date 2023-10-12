package com.openwt.officetracking.persistent.office;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "offices")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OfficeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "office_uuid")
    private String officeUUID;

    private String name;

    private Double latitude;

    private Double longitude;

    private Double radius;
}
