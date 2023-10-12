package com.openwt.officetracking.api.device;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRequestAcceptDTO {

    private UUID requestId;

    private UUID deviceId;
}
