package com.openwt.officetracking.domain.event;

import com.openwt.officetracking.api.tracking_history.TrackingHistoryDetailDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TrackingHistoryEvent extends ApplicationEvent {

    private final TrackingHistoryDetailDTO history;

    public TrackingHistoryEvent(final Object source, final TrackingHistoryDetailDTO history) {
        super(source);
        this.history = history;
    }
}
