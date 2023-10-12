package com.openwt.officetracking.domain.mentorship;

import com.openwt.officetracking.error.BadRequestException;
import org.junit.jupiter.api.Test;

import static com.openwt.officetracking.domain.mentorship.MentorValidate.validateUserId;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MentorValidateTest {

    @Test
    void validateUserId_OK() {
        assertDoesNotThrow(() -> validateUserId(randomUUID()));
    }

    @Test
    void validateUserId_ThroughBadRequest() {
        assertThrows(BadRequestException.class, () -> validateUserId(null));
    }
}
