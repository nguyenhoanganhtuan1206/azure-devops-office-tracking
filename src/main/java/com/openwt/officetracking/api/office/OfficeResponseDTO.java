package com.openwt.officetracking.api.office;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class OfficeResponseDTO {

    private UUID id;

    private String name;

    private String officeUUID;

    private Double latitude;

    private Double longitude;

    private Double radius;
}