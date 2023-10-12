package com.openwt.officetracking.api.sse;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockAdmin;
import com.openwt.officetracking.domain.sse.SseEmitterManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static java.util.UUID.randomUUID;

@WebMvcTest(SSEController.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SseEmitterManager.class})
class SSEControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/sse";

    @Test
    @WithMockAdmin
    void shouldGetUpdatedTrackingHistory_OK() throws Exception {
        final var subscriberId = randomUUID();

        get(BASE_URL + "/history-update/" + subscriberId);
    }
}