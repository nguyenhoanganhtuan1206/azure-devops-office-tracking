package com.openwt.officetracking.domain.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ResourceUtilsTest {

    @Test
    public void readResource_OK() {
        final String resourceContent = ResourceUtils.readResource("/templates/ActivationEmail.html");
        assertNotNull(resourceContent);
    }

    @Test
    public void readResource_ThrowFailedToRead() {
        final String path = "test.html";
        assertThrows(RuntimeException.class, () -> ResourceUtils.readResource(path));
    }
}