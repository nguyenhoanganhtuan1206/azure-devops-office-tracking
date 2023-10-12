package com.openwt.officetracking.domain.device;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class DeviceRequestReject {

    private UUID requestId;

    private String rejectNote;
}
