package com.openwt.officetracking.domain.attendance;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.UUID;

@Getter
@Setter
@With
@Builder
public class AttendanceBiometryRequest {

    private UUID officeId;

    private String biometryToken;
}
