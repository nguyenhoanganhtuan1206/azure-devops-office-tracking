package com.openwt.officetracking.api.device;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRequestRejectDTO {

    private UUID requestId;

    private String rejectNote;
}
