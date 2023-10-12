package com.openwt.officetracking.fake;

import com.openwt.officetracking.domain.image.Image;
import lombok.experimental.UtilityClass;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class ImageFakes {

    public static Image buildImage() {
        return Image.builder()
                .id(randomUUID())
                .url(randomAlphabetic(6, 10))
                .userId(randomUUID())
                .build();
    }
}
