package com.openwt.officetracking.domain.office;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.UUID;

@Getter
@Setter
@Builder
@With
public class Office {

    private UUID id;

    private String officeUUID;

    private String name;

    private Double latitude;

    private Double longitude;

    private Double radius;
}
