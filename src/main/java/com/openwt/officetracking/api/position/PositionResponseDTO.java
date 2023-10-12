package com.openwt.officetracking.api.position;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class PositionResponseDTO {

    private UUID id;

    private String name;

    private boolean isTemporary;
}