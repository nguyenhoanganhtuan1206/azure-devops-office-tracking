package com.openwt.officetracking.api.sse;

import com.openwt.officetracking.domain.sse.SseEmitterManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sse")
@RequiredArgsConstructor
public class SSEController {

    private final SseEmitterManager sseEmitterManager;

    @GetMapping(value = "history-update/{subscriberId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getUpdatedTrackingHistory(@PathVariable final UUID subscriberId) {
        final SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitter.onCompletion(() -> sseEmitterManager.removeEmitter(subscriberId));
        emitter.onTimeout(() -> {
            emitter.complete();
            sseEmitterManager.removeEmitter(subscriberId);
        });
        sseEmitterManager.addEmitter(subscriberId, emitter);
        return emitter;
    }
}
