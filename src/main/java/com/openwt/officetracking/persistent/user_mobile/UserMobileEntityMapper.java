package com.openwt.officetracking.persistent.user_mobile;

import com.openwt.officetracking.domain.user_mobile.UserMobile;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class UserMobileEntityMapper {

    public static UserMobile toUserMobile(final UserMobileEntity userMobileEntity) {
        return UserMobile.builder()
                .id(userMobileEntity.getId())
                .userId(userMobileEntity.getUserId())
                .deviceType(userMobileEntity.getDeviceType())
                .model(userMobileEntity.getModel())
                .osVersion(userMobileEntity.getOsVersion())
                .biometryToken(userMobileEntity.getBiometryToken())
                .fcmToken(userMobileEntity.getFcmToken())
                .isActive(userMobileEntity.isActive())
                .registeredAt(userMobileEntity.getRegisteredAt())
                .isEnableBluetooth(userMobileEntity.isEnableBluetooth())
                .isEnableLocation(userMobileEntity.isEnableLocation())
                .isEnableBackground(userMobileEntity.isEnableBackground())
                .isEnableNotification(userMobileEntity.isEnableNotification())
                .build();
    }

    public static List<UserMobile> toUserMobileList(final List<UserMobileEntity> userMobileEntities) {
        return userMobileEntities.stream()
                .map(UserMobileEntityMapper::toUserMobile)
                .toList();
    }

    public static UserMobileEntity toUserMobileEntity(final UserMobile userMobile) {
        return UserMobileEntity.builder()
                .id(userMobile.getId())
                .userId(userMobile.getUserId())
                .deviceType(userMobile.getDeviceType())
                .model(userMobile.getModel())
                .osVersion(userMobile.getOsVersion())
                .biometryToken(userMobile.getBiometryToken())
                .fcmToken(userMobile.getFcmToken())
                .isActive(userMobile.isActive())
                .registeredAt(userMobile.getRegisteredAt())
                .isEnableBluetooth(userMobile.isEnableBluetooth())
                .isEnableLocation(userMobile.isEnableLocation())
                .isEnableBackground(userMobile.isEnableBackground())
                .isEnableNotification(userMobile.isEnableNotification())
                .build();
    }
}
