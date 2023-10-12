package com.openwt.officetracking.domain.position;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.UUID;

@Builder
@Getter
@Setter
@With
public class Position {

    private UUID id;

    private String name;

    private boolean isTemporary;
}
