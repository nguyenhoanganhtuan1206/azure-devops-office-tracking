package com.openwt.officetracking.domain.utils;

import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@UtilityClass
public class ResourceUtils {

    public static String readResource(final String path) {
        try {
            final ClassPathResource resource = new ClassPathResource(path);
            final InputStream inputStream = resource.getInputStream();
            final byte[] resourceBytes = inputStream.readAllBytes();
            return new String(resourceBytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read resource " + path, e);
        }
    }
}
