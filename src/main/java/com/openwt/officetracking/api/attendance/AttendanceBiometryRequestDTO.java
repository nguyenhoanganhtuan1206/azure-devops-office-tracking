package com.openwt.officetracking.api.attendance;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class AttendanceBiometryRequestDTO {

    private UUID officeId;

    private String biometryToken;
}
