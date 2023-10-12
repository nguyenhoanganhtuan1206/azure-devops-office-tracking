package com.openwt.officetracking.domain.sse;

import com.openwt.officetracking.domain.event.TrackingHistoryEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CopyOnWriteArrayList;

import static com.openwt.officetracking.fake.TrackingHistoryFakes.buildTrackingHistoryDetailDTO;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SseEmitterManagerTest {

    @InjectMocks
    private SseEmitterManager sseEmitterManager;

    @Test
    void shouldAddEmitter_OK() {
        final var emitterMock = mock(SseEmitter.class);

        sseEmitterManager.addEmitter(randomUUID(), emitterMock);
    }

    @Test
    void shouldRemoveEmitter_OK() {
        sseEmitterManager.removeEmitter(randomUUID());
    }

    @Test
    void shouldHandleHistoryEvent_OK() {
        final var historyResponse = buildTrackingHistoryDetailDTO();
        final var emitters = new CopyOnWriteArrayList<>();
        final var emitterMock = mock(SseEmitter.class);

        emitters.add(emitterMock);

        sseEmitterManager.handleHistoryEvent(new TrackingHistoryEvent(this, historyResponse));

        final SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event();
        eventBuilder.data(historyResponse, MediaType.APPLICATION_JSON);
        eventBuilder.name("history-update");
    }
}