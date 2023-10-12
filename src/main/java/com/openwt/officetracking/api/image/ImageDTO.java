package com.openwt.officetracking.api.image;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ImageDTO {

    private UUID id;

    private String url;

    private UUID userId;
}
