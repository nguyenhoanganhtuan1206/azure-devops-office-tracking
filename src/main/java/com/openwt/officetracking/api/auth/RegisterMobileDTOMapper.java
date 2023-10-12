package com.openwt.officetracking.api.auth;

import com.openwt.officetracking.domain.auth.RegisterMobile;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RegisterMobileDTOMapper {

    public static RegisterMobile toRegisterMobile(final RegisterMobileDTO registerMobileDTO) {
        return RegisterMobile.builder()
                .email(registerMobileDTO.getEmail())
                .biometryToken(registerMobileDTO.getBiometryToken())
                .deviceType(registerMobileDTO.getDeviceType())
                .model(registerMobileDTO.getModel())
                .osVersion(registerMobileDTO.getOsVersion())
                .fcmToken(registerMobileDTO.getFcmToken())
                .build();
    }
}
