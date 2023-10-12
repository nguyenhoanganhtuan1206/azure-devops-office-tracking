package com.openwt.officetracking.api.office;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfficeRequestDTO {

    private String officeUUID;

    private String name;

    private Double longitude;

    private Double latitude;

    private Double radius;
}
