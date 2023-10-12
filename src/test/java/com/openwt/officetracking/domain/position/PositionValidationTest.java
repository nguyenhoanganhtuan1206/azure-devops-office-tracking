package com.openwt.officetracking.domain.position;

import com.openwt.officetracking.error.BadRequestException;
import org.junit.jupiter.api.Test;

import static com.openwt.officetracking.domain.position.PositionValidation.validate;
import static com.openwt.officetracking.fake.PositionFakes.buildPosition;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PositionValidationTest {

    @Test
    void validate_OK() {
        final var position = buildPosition()
                .withName("Intern")
                .withTemporary(true);

        assertDoesNotThrow(() -> validate(position));
    }

    @Test
    void validate_ThrowNameEmpty() {
        final var position = buildPosition()
                .withName(null);

        assertThrows(BadRequestException.class, () -> validate(position));
    }
}