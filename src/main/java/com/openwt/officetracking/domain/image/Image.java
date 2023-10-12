package com.openwt.officetracking.domain.image;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.UUID;

@Getter
@Setter
@Builder
@With
public class Image {

    private UUID id;

    private String url;

    private UUID userId;
}
