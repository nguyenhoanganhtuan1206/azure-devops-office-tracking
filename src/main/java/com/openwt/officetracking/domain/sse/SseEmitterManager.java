package com.openwt.officetracking.domain.sse;

import com.openwt.officetracking.api.tracking_history.TrackingHistoryDetailDTO;
import com.openwt.officetracking.domain.event.TrackingHistoryEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SseEmitterManager {

    private final Map<UUID, SseEmitter> emitters = new HashMap<>();

    public void addEmitter(final UUID subscriberId, final SseEmitter emitter) {
        emitters.put(subscriberId, emitter);
    }

    public void removeEmitter(final UUID subscriberId) {
        emitters.remove(subscriberId);
    }

    @EventListener
    public void handleHistoryEvent(final TrackingHistoryEvent historyEvent) {
        final TrackingHistoryDetailDTO history = historyEvent.getHistory();

        for (final SseEmitter emitter : emitters.values()) {
            try {
                emitter.send(history, MediaType.APPLICATION_JSON);
            } catch (final IOException e) {
                emitter.complete();
                throw new RuntimeException("An error occurred while processing with sse. Please try again later", e);
            }
        }
    }
}
