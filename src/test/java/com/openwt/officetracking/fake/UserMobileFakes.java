package com.openwt.officetracking.fake;

import com.openwt.officetracking.api.user_mobile.UpdateUserMobileDTO;
import com.openwt.officetracking.domain.user_mobile.UpdateMobilePermission;
import com.openwt.officetracking.domain.user_mobile.UserMobile;
import com.openwt.officetracking.domain.user_mobile.UserMobileDetail;
import com.openwt.officetracking.persistent.user_mobile.UserMobileEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static com.openwt.officetracking.fake.CommonFakes.randomInstant;
import static com.openwt.officetracking.fake.UserFakes.buildUserDetail;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class UserMobileFakes {

    public static UserMobile buildUserMobile() {
        return UserMobile.builder()
                .id(randomUUID())
                .userId(randomUUID())
                .deviceType(randomAlphabetic(3, 10))
                .model(randomAlphabetic(3, 10))
                .osVersion(randomAlphabetic(3, 10))
                .biometryToken(randomAlphabetic(3, 10))
                .fcmToken(randomAlphabetic(3, 10))
                .isActive(true)
                .registeredAt(randomInstant())
                .build();
    }

    public static List<UserMobile> buildUserMobiles() {
        return buildList(UserMobileFakes::buildUserMobile);
    }

    public static UserMobileEntity buildUserMobileEntity() {
        return UserMobileEntity.builder()
                .id(randomUUID())
                .userId(randomUUID())
                .deviceType(randomAlphabetic(3, 10))
                .model(randomAlphabetic(3, 10))
                .osVersion(randomAlphabetic(3, 10))
                .biometryToken(randomAlphabetic(3, 10))
                .fcmToken(randomAlphabetic(3, 10))
                .isActive(true)
                .registeredAt(randomInstant())
                .build();
    }

    public static UpdateUserMobileDTO buildUpdateUserMobileDTO() {
        return UpdateUserMobileDTO.builder()
                .model(randomAlphabetic(3, 10))
                .deviceType(randomAlphabetic(3, 10))
                .osVersion(randomAlphabetic(3, 10))
                .fcmToken(randomAlphabetic(3, 10))
                .build();
    }

    public static List<UserMobileEntity> buildUserMobileEntities() {
        return buildList(UserMobileFakes::buildUserMobileEntity);
    }

    public static UserMobileDetail buildUserMobileDetail() {
        return UserMobileDetail.builder()
                .id(randomUUID())
                .deviceType(randomAlphabetic(3, 10))
                .Model(randomAlphabetic(3, 10))
                .osVersion(randomAlphabetic(3, 10))
                .registeredAt(randomInstant())
                .owner(buildUserDetail())
                .build();
    }

    public static UpdateMobilePermission buildUpdateMobilePermission() {
        return UpdateMobilePermission.builder()
                .isEnableBluetooth(true)
                .isEnableLocation(true)
                .isEnableBackground(true)
                .isEnableNotification(true)
                .build();
    }
}
