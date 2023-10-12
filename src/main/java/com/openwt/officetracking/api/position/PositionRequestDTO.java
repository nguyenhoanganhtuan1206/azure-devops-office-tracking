package com.openwt.officetracking.api.position;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PositionRequestDTO {

    private String name;

    private boolean isTemporary;
}
