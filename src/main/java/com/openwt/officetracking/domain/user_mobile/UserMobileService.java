package com.openwt.officetracking.domain.user_mobile;

import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.position.Position;
import com.openwt.officetracking.domain.position.PositionService;
import com.openwt.officetracking.domain.user.User;
import com.openwt.officetracking.domain.user.UserDetail;
import com.openwt.officetracking.persistent.user.UserStore;
import com.openwt.officetracking.persistent.user_mobile.UserMobileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.domain.user_mobile.UserMobileError.supplyUserMobileNotFound;
import static com.openwt.officetracking.domain.user_mobile.UserMobileValidation.validateUserMobileWhenRegister;
import static com.openwt.officetracking.domain.user_mobile.UserMobileValidation.validateUserMobileWhenUpdate;

@Service
@RequiredArgsConstructor
public class UserMobileService {

    private final UserMobileStore userMobileStore;

    private final UserStore userStore;

    private final PositionService positionService;

    private final AuthsProvider authsProvider;

    public UserMobile findById(final UUID id) {
        return userMobileStore.findById(id)
                .orElseThrow(supplyUserMobileNotFound("id", id));
    }

    public UserMobile findByBiometryToken(final String biometryToken) {
        return userMobileStore.findByBiometryToken(biometryToken)
                .orElseThrow(supplyUserMobileNotFound("Biometry Token", biometryToken));
    }

    public List<UserMobile> findAll() {
        return userMobileStore.findAll();
    }

    public UserMobile findByUserId(final UUID userId) {
        return userMobileStore.findByUserId(userId)
                .orElseThrow(supplyUserMobileNotFound("user id", userId));
    }

    public UserMobileDetail findDetailById(final UUID id) {
        final UserMobile userMobile = findById(id);
        final User userDetail = userStore.findById(userMobile.getUserId()).get();
        final Position userPosition = positionService.findById(userDetail.getPositionId());

        return buildUserMobileDetail(userMobile, userDetail, userPosition);
    }

    public UserMobile update(final UserMobile updateMobile) {
        validateUserMobileWhenUpdate(updateMobile);

        final UserMobile currentMobile = findByUserId(authsProvider.getCurrentUserId());

        currentMobile.setModel(updateMobile.getModel());
        currentMobile.setDeviceType(updateMobile.getDeviceType());
        currentMobile.setOsVersion(updateMobile.getOsVersion());
        currentMobile.setFcmToken(updateMobile.getFcmToken());

        return userMobileStore.save(currentMobile);
    }

    public UserMobile registerUserMobile(final UserMobile updateMobile) {
        validateUserMobileWhenRegister(updateMobile);

        final Optional<UserMobile> userMobileOpt = userMobileStore.findByUserId(updateMobile.getUserId());

        final UserMobile userMobile = userMobileOpt.orElse(UserMobile.builder()
                .userId(updateMobile.getUserId())
                .build());

        updateUserMobileProperties(userMobile, updateMobile);

        return userMobileStore.save(userMobile);
    }

    public UserMobile create(final UserMobile userMobile) {
        return userMobileStore.save(userMobile);
    }

    public UpdateMobilePermission updateMobilePermissions(final UpdateMobilePermission updatePermission) {
        final UserMobile currentUserMobile = findByUserId(authsProvider.getCurrentUserId());

        if (updatePermission.getIsEnableBluetooth() != null) {
            currentUserMobile.setEnableBluetooth(updatePermission.getIsEnableBluetooth());
        }

        if (updatePermission.getIsEnableLocation() != null) {
            currentUserMobile.setEnableLocation(updatePermission.getIsEnableLocation());
        }

        if (updatePermission.getIsEnableBackground() != null) {
            currentUserMobile.setEnableBackground(updatePermission.getIsEnableBackground());
        }

        if (updatePermission.getIsEnableNotification() != null) {
            currentUserMobile.setEnableNotification(updatePermission.getIsEnableNotification());
        }

        final UserMobile updatedMobile = userMobileStore.save(currentUserMobile);

        return UpdateMobilePermission.builder()
                .isEnableBluetooth(updatedMobile.isEnableBluetooth())
                .isEnableLocation(updatedMobile.isEnableLocation())
                .isEnableBackground(updatedMobile.isEnableBackground())
                .isEnableNotification(updatedMobile.isEnableNotification())
                .build();
    }

    private UserMobileDetail buildUserMobileDetail(final UserMobile userMobile, final User user, final Position position) {
        return UserMobileDetail.builder()
                .id(userMobile.getId())
                .deviceType(userMobile.getDeviceType())
                .Model(userMobile.getModel())
                .osVersion(userMobile.getOsVersion())
                .registeredAt(userMobile.getRegisteredAt())
                .owner(buildUserDetail(user, position))
                .build();
    }

    public static UserDetail buildUserDetail(final User user, final Position position) {
        return UserDetail.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .identifier(user.getIdentifier())
                .phoneNumber(user.getPhoneNumber())
                .personalEmail(user.getPersonalEmail())
                .companyEmail(user.getCompanyEmail())
                .contractType(user.getContractType())
                .positionName(position.getName())
                .startDate(user.getStartDate())
                .endDate(user.getEndDate())
                .build();
    }

    private void updateUserMobileProperties(final UserMobile userMobile, final UserMobile updateMobile) {
        userMobile.setBiometryToken(updateMobile.getBiometryToken());
        userMobile.setDeviceType(updateMobile.getDeviceType());
        userMobile.setModel(updateMobile.getModel());
        userMobile.setOsVersion(updateMobile.getOsVersion());
        userMobile.setFcmToken(updateMobile.getFcmToken());
        userMobile.setActive(updateMobile.isActive());

        if (userMobile.getRegisteredAt() == null) {
            userMobile.setRegisteredAt(updateMobile.getRegisteredAt());
        }
    }
}
