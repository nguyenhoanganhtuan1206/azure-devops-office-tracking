package com.openwt.officetracking.api.door;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class DoorRequestDTO {

    private String name;

    private int major;

    private int minor;

    private String note;

    private UUID officeId;
}