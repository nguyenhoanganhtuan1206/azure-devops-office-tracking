package com.openwt.officetracking.domain.utils;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static com.openwt.officetracking.domain.utils.Base64Utils.decodeBase64;
import static com.openwt.officetracking.domain.utils.Base64Utils.isBase64;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.*;

class Base64UtilsTest {

    @Test
    public void testIsBase64(){
        final String base64String = "data:image/png;base64,Test";
        final String notBase64String = randomAlphabetic(6, 10);
        assertTrue(isBase64(base64String));
        assertFalse(isBase64(notBase64String));
    }

    @Test
    public void testDecodeBase64_OK(){
        final var input = Base64.getEncoder().encode("Test".getBytes());
        final var photo = String.format("%s,%s", "data:image/png;base64", new String(input));
        final var expected = "Test".getBytes();
        final var actual = decodeBase64(photo);
        assertArrayEquals(expected, actual);
    }
}