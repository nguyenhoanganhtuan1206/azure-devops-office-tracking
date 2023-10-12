package com.openwt.officetracking.api.user_mobile;

import com.openwt.officetracking.domain.user_mobile.UpdateMobilePermission;
import com.openwt.officetracking.domain.user_mobile.UserMobile;
import com.openwt.officetracking.domain.user_mobile.UserMobileDetail;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.api.user.UserDTOMapper.toUserDetailDTO;

@UtilityClass
public class UserMobileDTOMapper {

    public static UserMobile toUserMobile(final UpdateUserMobileDTO updateUserMobileDTO) {
        return UserMobile.builder()
                .model(updateUserMobileDTO.getModel())
                .deviceType(updateUserMobileDTO.getDeviceType())
                .osVersion(updateUserMobileDTO.getOsVersion())
                .fcmToken(updateUserMobileDTO.getFcmToken())
                .build();
    }

    public static UpdateUserMobileDTO toUpdateUserMobileDTO(final UserMobile userMobile) {
        return UpdateUserMobileDTO.builder()
                .model(userMobile.getModel())
                .deviceType(userMobile.getDeviceType())
                .osVersion(userMobile.getOsVersion())
                .fcmToken(userMobile.getFcmToken())
                .build();
    }

    public static UserMobileDTO toUserMobileDTO(final UserMobile userMobile) {
        return UserMobileDTO.builder()
                .id(userMobile.getId())
                .userId(userMobile.getUserId())
                .deviceType(userMobile.getDeviceType())
                .model(userMobile.getModel())
                .osVersion(userMobile.getOsVersion())
                .isActive(userMobile.isActive())
                .registeredAt(userMobile.getRegisteredAt())
                .build();
    }

    public static List<UserMobileDTO> toUserMobileDTOs(final List<UserMobile> userMobileList) {
        return userMobileList.stream()
                .map(UserMobileDTOMapper::toUserMobileDTO)
                .toList();
    }

    public static UserMobileDetailDTO toUserMobileDetailDTO(final UserMobileDetail userMobileDetail) {
        return UserMobileDetailDTO.builder()
                .id(userMobileDetail.getId())
                .deviceType(userMobileDetail.getDeviceType())
                .Model(userMobileDetail.getModel())
                .osVersion(userMobileDetail.getOsVersion())
                .registeredAt(userMobileDetail.getRegisteredAt())
                .owner(toUserDetailDTO(userMobileDetail.getOwner()))
                .build();
    }

    public static UpdateMobilePermissionDTO toUpdateMobilePermissionDTO(final UpdateMobilePermission updateMobilePermission) {
        return UpdateMobilePermissionDTO.builder()
                .isEnableBluetooth(updateMobilePermission.getIsEnableBluetooth())
                .isEnableLocation(updateMobilePermission.getIsEnableLocation())
                .isEnableBackground(updateMobilePermission.getIsEnableBackground())
                .isEnableNotification(updateMobilePermission.getIsEnableNotification())
                .build();
    }

    public static UpdateMobilePermission toUpdateMobilePermission(final UpdateMobilePermissionDTO updateMobilePermissionDTO) {
        return UpdateMobilePermission.builder()
                .isEnableBluetooth(updateMobilePermissionDTO.getIsEnableBluetooth())
                .isEnableLocation(updateMobilePermissionDTO.getIsEnableLocation())
                .isEnableBackground(updateMobilePermissionDTO.getIsEnableBackground())
                .isEnableNotification(updateMobilePermissionDTO.getIsEnableNotification())
                .build();
    }
}
