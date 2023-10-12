package com.openwt.officetracking.domain.user_mobile;

import lombok.experimental.UtilityClass;

import static com.openwt.officetracking.error.CommonError.supplyValidationError;
import static org.apache.commons.lang3.StringUtils.isBlank;

@UtilityClass
public class UserMobileValidation {

    public static void validateUserMobileWhenUpdate(final UserMobile userMobile) {
        validateDeviceType(userMobile.getDeviceType());
        validateModel(userMobile.getModel());
        validateOsVersion(userMobile.getOsVersion());
        validateFcmToken(userMobile.getFcmToken());
    }

    public static void validateUserMobileWhenRegister(final UserMobile userMobile) {
        validateBiometryToken(userMobile.getBiometryToken());
        validateDeviceType(userMobile.getDeviceType());
        validateModel(userMobile.getModel());
        validateOsVersion(userMobile.getOsVersion());
        validateFcmToken(userMobile.getFcmToken());
    }

    private static void validateBiometryToken(final String biometryToken) {
        if (isBlank(biometryToken)) {
            throw supplyValidationError("Biometry token cannot be blank").get();
        }
    }

    private static void validateDeviceType(final String deviceType) {
        if (isBlank(deviceType)) {
            throw supplyValidationError("Device type cannot be blank").get();
        }
    }

    private static void validateModel(final String model) {
        if (isBlank(model)) {
            throw supplyValidationError("Device model cannot be blank").get();
        }
    }

    private static void validateOsVersion(final String osVersion) {
        if (isBlank(osVersion)) {
            throw supplyValidationError("Device os version cannot be blank").get();
        }
    }

    private static void validateFcmToken(final String fcmToken) {
        if (isBlank(fcmToken)) {
            throw supplyValidationError("Fcm token version cannot be blank").get();
        }
    }
}
