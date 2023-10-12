package com.openwt.officetracking.fake;

import com.openwt.officetracking.api.auth.RegisterMobileDTO;
import com.openwt.officetracking.domain.auth.RegisterMobile;
import lombok.experimental.UtilityClass;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class RegisterMobileFakes {

    public static RegisterMobile buildRegisterMobile() {
        return RegisterMobile.builder()
                .email(randomAlphabetic(3, 10))
                .biometryToken(randomAlphabetic(3, 10))
                .deviceType(randomAlphabetic(3, 10))
                .model(randomAlphabetic(3, 10))
                .osVersion(randomAlphabetic(3, 10))
                .fcmToken(randomAlphabetic(3, 10))
                .build();
    }

    public static RegisterMobileDTO buildRegisterMobileDTO() {
        return RegisterMobileDTO.builder()
                .email(randomAlphabetic(3, 10))
                .biometryToken(randomAlphabetic(3, 10))
                .deviceType(randomAlphabetic(3, 10))
                .model(randomAlphabetic(3, 10))
                .osVersion(randomAlphabetic(3, 10))
                .fcmToken(randomAlphabetic(3, 10))
                .build();
    }
}
