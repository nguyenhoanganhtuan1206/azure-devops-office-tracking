package com.openwt.officetracking.domain.fcm;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

@Getter
@Setter
@Builder
@With
public class NotificationMessage {

    private String title;

    private String body;
}
