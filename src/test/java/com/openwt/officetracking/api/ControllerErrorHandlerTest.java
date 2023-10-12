package com.openwt.officetracking.api;

import com.openwt.officetracking.error.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ControllerErrorHandlerTest {

    private final ControllerErrorHandler controllerErrorHandling = new ControllerErrorHandler();

    @Test
    void shouldHandleDomainException_OK() {
        final var error = new NotFoundException("Message");
        final var response = controllerErrorHandling.handleDomainError(error);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody());

        assertEquals("Message", response.getBody().getMessage());
        assertNotNull(response.getBody().getOccurAt());
    }
}
