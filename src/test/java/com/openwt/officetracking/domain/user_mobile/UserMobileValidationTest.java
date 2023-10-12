package com.openwt.officetracking.domain.user_mobile;

import com.openwt.officetracking.error.BadRequestException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.openwt.officetracking.domain.user_mobile.UserMobileValidation.validateUserMobileWhenUpdate;
import static com.openwt.officetracking.domain.user_mobile.UserMobileValidation.validateUserMobileWhenRegister;
import static com.openwt.officetracking.fake.UserMobileFakes.buildUserMobile;
import static org.junit.jupiter.api.Assertions.*;

class UserMobileValidationTest {

    private static Stream<Arguments> provideInvalidUserMobile() {
        return Stream.of(
                Arguments.of(buildUserMobile().withBiometryToken("")),
                Arguments.of(buildUserMobile().withDeviceType("")),
                Arguments.of(buildUserMobile().withModel("")),
                Arguments.of(buildUserMobile().withOsVersion("")),
                Arguments.of(buildUserMobile().withFcmToken(""))
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUserMobile")
    void validate_ThrowValidationException(final UserMobile userMobile) {
        assertThrows(BadRequestException.class, () -> validateUserMobileWhenRegister(userMobile));
    }

    private static Stream<Arguments> provideInvalidUserMobileUpdate() {
        return Stream.of(
                Arguments.of(buildUserMobile().withDeviceType("")),
                Arguments.of(buildUserMobile().withModel("")),
                Arguments.of(buildUserMobile().withOsVersion("")),
                Arguments.of(buildUserMobile().withFcmToken(""))
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUserMobileUpdate")
    void validateUserMobileUpdate_ThrowValidationException(final UserMobile userMobile) {
        assertThrows(BadRequestException.class, () -> validateUserMobileWhenUpdate(userMobile));
    }
}