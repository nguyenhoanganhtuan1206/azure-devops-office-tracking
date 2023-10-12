package com.openwt.officetracking.domain.user_mobile;

import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.position.PositionService;
import com.openwt.officetracking.error.NotFoundException;
import com.openwt.officetracking.persistent.user.UserStore;
import com.openwt.officetracking.persistent.user_mobile.UserMobileStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static com.openwt.officetracking.fake.PositionFakes.buildPosition;
import static com.openwt.officetracking.fake.UserFakes.buildUser;
import static com.openwt.officetracking.fake.UserFakes.buildUserDetail;
import static com.openwt.officetracking.fake.UserMobileFakes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserMobileServiceTest {

    @InjectMocks
    private UserMobileService userMobileService;

    @Mock
    private UserMobileStore userMobileStore;

    @Mock
    private UserStore userStore;

    @Mock
    private PositionService positionService;

    @Mock
    private AuthsProvider authsProvider;

    @Test
    void shouldFindByBiometryToken_OK() {
        final var userMobile = buildUserMobile();

        when(userMobileStore.findByBiometryToken(userMobile.getBiometryToken()))
                .thenReturn(Optional.of(userMobile));

        final var actual = userMobileService.findByBiometryToken(userMobile.getBiometryToken());

        assertEquals(userMobile.getId(), actual.getId());
        assertEquals(userMobile.getUserId(), actual.getUserId());
        assertEquals(userMobile.getBiometryToken(), actual.getBiometryToken());
        assertEquals(userMobile.getModel(), actual.getModel());
        assertEquals(userMobile.getDeviceType(), actual.getDeviceType());
        assertEquals(userMobile.getBiometryToken(), actual.getBiometryToken());
        assertEquals(userMobile.getFcmToken(), actual.getFcmToken());
        assertEquals(userMobile.isActive(), actual.isActive());
        assertEquals(userMobile.getRegisteredAt(), actual.getRegisteredAt());

        verify(userMobileStore).findByBiometryToken(userMobile.getBiometryToken());
    }

    @Test
    public void shouldFindAllUserMobile_OK() {
        final var userDevices = buildUserMobiles();

        when(userMobileStore.findAll()).thenReturn(userDevices);

        final var actual = userMobileService.findAll();

        assertEquals(userDevices.size(), actual.size());
        assertEquals(userDevices.get(0).getId(), actual.get(0).getId());
        assertEquals(userDevices.get(0).getUserId(), actual.get(0).getUserId());
        assertEquals(userDevices.get(0).getDeviceType(), actual.get(0).getDeviceType());
        assertEquals(userDevices.get(0).getModel(), actual.get(0).getModel());
        assertEquals(userDevices.get(0).getOsVersion(), actual.get(0).getOsVersion());
        assertEquals(userDevices.get(0).getBiometryToken(), actual.get(0).getBiometryToken());
        assertEquals(userDevices.get(0).getFcmToken(), actual.get(0).getFcmToken());
        assertEquals(userDevices.get(0).isActive(), actual.get(0).isActive());
        assertEquals(userDevices.get(0).getRegisteredAt(), actual.get(0).getRegisteredAt());

        verify(userMobileStore).findAll();
    }

    @Test
    public void shouldCreateUserMobile_OK() {
        final var userDevice = buildUserMobile();

        when(userMobileStore.save(argThat(userDeviceArg -> userDevice.getId() == userDevice.getId())))
                .thenReturn(userDevice);

        final var actual = userMobileService.create(userDevice);

        assertEquals(userDevice.getId(), actual.getId());
        assertEquals(userDevice.getUserId(), actual.getUserId());
        assertEquals(userDevice.getDeviceType(), actual.getDeviceType());
        assertEquals(userDevice.getModel(), actual.getModel());
        assertEquals(userDevice.getOsVersion(), actual.getOsVersion());
        assertEquals(userDevice.getBiometryToken(), actual.getBiometryToken());
        assertEquals(userDevice.getFcmToken(), actual.getFcmToken());
        assertEquals(userDevice.isActive(), actual.isActive());
        assertEquals(userDevice.getRegisteredAt(), actual.getRegisteredAt());

        verify(userMobileStore).save(argThat(userDeviceArg -> userDevice.getId() == userDevice.getId()));
    }

    @Test
    public void shouldFindByUserId_OK() {
        final var userDevice = buildUserMobile();

        when(userMobileStore.findByUserId(userDevice.getUserId())).thenReturn(Optional.of(userDevice));

        final var actual = userMobileService.findByUserId(userDevice.getUserId());

        assertEquals(userDevice.getId(), actual.getId());
        assertEquals(userDevice.getUserId(), actual.getUserId());
        assertEquals(userDevice.getDeviceType(), actual.getDeviceType());
        assertEquals(userDevice.getModel(), actual.getModel());
        assertEquals(userDevice.getOsVersion(), actual.getOsVersion());
        assertEquals(userDevice.getBiometryToken(), actual.getBiometryToken());
        assertEquals(userDevice.getFcmToken(), actual.getFcmToken());
        assertEquals(userDevice.isActive(), actual.isActive());
        assertEquals(userDevice.getRegisteredAt(), actual.getRegisteredAt());

        verify(userMobileStore).findByUserId(userDevice.getUserId());
    }

    @Test
    public void shouldFindByUserId_ThrowNotFound() {
        final var userDevice = buildUserMobile();

        when(userMobileStore.findByUserId(userDevice.getUserId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userMobileService.findByUserId(userDevice.getUserId()));

        verify(userMobileStore).findByUserId(userDevice.getUserId());
    }

    @Test
    public void shouldRegisterUserMobile_OK() {
        final var userDevice = buildUserMobile();

        when(userMobileStore.findByUserId(userDevice.getUserId())).thenReturn(Optional.of(userDevice));
        when(userMobileStore.save(argThat(mobileArg -> mobileArg.getId() == userDevice.getId()))).thenReturn(userDevice);

        final var actual = userMobileService.registerUserMobile(userDevice);

        assertEquals(userDevice.getId(), actual.getId());
        assertEquals(userDevice.getUserId(), actual.getUserId());
        assertEquals(userDevice.getDeviceType(), actual.getDeviceType());
        assertEquals(userDevice.getModel(), actual.getModel());
        assertEquals(userDevice.getOsVersion(), actual.getOsVersion());
        assertEquals(userDevice.getBiometryToken(), actual.getBiometryToken());
        assertEquals(userDevice.getFcmToken(), actual.getFcmToken());
        assertEquals(userDevice.isActive(), actual.isActive());
        assertEquals(userDevice.getRegisteredAt(), actual.getRegisteredAt());

        verify(userMobileStore).findByUserId(userDevice.getUserId());
        verify(userMobileStore).save(argThat(mobileArg -> mobileArg.getId() == userDevice.getId()));
    }

    @Test
    public void shouldRegisterUserMobile_WithOldUserAccount() {
        final var userDevice = buildUserMobile();

        when(userMobileStore.findByUserId(userDevice.getUserId())).thenReturn(Optional.empty());
        when(userMobileStore.save(any(UserMobile.class))).thenReturn(userDevice);

        final var actual = userMobileService.registerUserMobile(userDevice);

        assertEquals(userDevice.getId(), actual.getId());
        assertEquals(userDevice.getUserId(), actual.getUserId());
        assertEquals(userDevice.getDeviceType(), actual.getDeviceType());
        assertEquals(userDevice.getModel(), actual.getModel());
        assertEquals(userDevice.getOsVersion(), actual.getOsVersion());
        assertEquals(userDevice.getBiometryToken(), actual.getBiometryToken());
        assertEquals(userDevice.getFcmToken(), actual.getFcmToken());
        assertEquals(userDevice.isActive(), actual.isActive());
        assertEquals(userDevice.getRegisteredAt(), actual.getRegisteredAt());

        verify(userMobileStore).findByUserId(userDevice.getUserId());
        verify(userMobileStore).save(any(UserMobile.class));
    }

    @Test
    public void shouldFindById_OK() {
        final var userDevice = buildUserMobile();

        when(userMobileStore.findById(userDevice.getUserId())).thenReturn(Optional.of(userDevice));

        final var actual = userMobileService.findById(userDevice.getUserId());

        assertEquals(userDevice.getId(), actual.getId());
        assertEquals(userDevice.getUserId(), actual.getUserId());
        assertEquals(userDevice.getDeviceType(), actual.getDeviceType());
        assertEquals(userDevice.getModel(), actual.getModel());
        assertEquals(userDevice.getOsVersion(), actual.getOsVersion());
        assertEquals(userDevice.getRegisteredAt(), actual.getRegisteredAt());

        verify(userMobileStore).findById(userDevice.getUserId());
    }

    @Test
    public void shouldFindById_ThrowNotFound() {
        final var userDevice = buildUserMobile();

        when(userMobileStore.findById(userDevice.getUserId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userMobileService.findById(userDevice.getUserId()));

        verify(userMobileStore).findById(userDevice.getUserId());
    }

    @Test
    public void shouldUpdateUserMobile_OK() {
        final var userDevice = buildUserMobile();

        when(authsProvider.getCurrentUserId()).thenReturn(userDevice.getUserId());
        when(userMobileStore.findByUserId(userDevice.getUserId())).thenReturn(Optional.of(userDevice));
        when(userMobileStore.save(argThat(mobileArg -> mobileArg.getUserId() == userDevice.getUserId()))).thenReturn(userDevice);

        final var actual = userMobileService.update(userDevice);

        assertEquals(userDevice.getId(), actual.getId());
        assertEquals(userDevice.getUserId(), actual.getUserId());
        assertEquals(userDevice.getDeviceType(), actual.getDeviceType());
        assertEquals(userDevice.getModel(), actual.getModel());
        assertEquals(userDevice.getOsVersion(), actual.getOsVersion());
        assertEquals(userDevice.getBiometryToken(), actual.getBiometryToken());
        assertEquals(userDevice.getFcmToken(), actual.getFcmToken());
        assertEquals(userDevice.isActive(), actual.isActive());
        assertEquals(userDevice.getRegisteredAt(), actual.getRegisteredAt());

        verify(authsProvider).getCurrentUserId();
        verify(userMobileStore).findByUserId(userDevice.getUserId());
        verify(userMobileStore).save(argThat(mobileArg -> mobileArg.getUserId() == userDevice.getUserId()));
    }

    @Test
    public void shouldFindDetailById_OK() {
        final var position = buildPosition();
        final var user = buildUser()
                .withPositionId(position.getId());
        final var userDetail = buildUserDetail()
                .withId(user.getId());
        final var userDevice = buildUserMobile()
                .withUserId(user.getId());
        final var userMobileDetail = buildUserMobileDetail()
                .withId(userDevice.getId())
                .withOwner(userDetail);


        when(userMobileStore.findById(userMobileDetail.getId())).thenReturn(Optional.of(userDevice));
        when(userStore.findById(userDevice.getUserId())).thenReturn(Optional.of(user));
        when(positionService.findById(user.getPositionId())).thenReturn(position);

        final var actual = userMobileService.findDetailById(userMobileDetail.getId());

        assertEquals(userMobileDetail.getId(), actual.getId());
        assertEquals(userMobileDetail.getOwner().getId(), actual.getOwner().getId());

        verify(userMobileStore).findById(userMobileDetail.getId());
        verify(userStore).findById(userDevice.getUserId());
        verify(positionService).findById(user.getPositionId());
    }

    private static Stream<Arguments> provideUserMobilePermission() {
        return Stream.of(
                Arguments.of(buildUpdateMobilePermission()
                        .withIsEnableBackground(true)
                        .withIsEnableBluetooth(true)
                        .withIsEnableLocation(true)
                        .withIsEnableNotification(true)),
                Arguments.of(buildUpdateMobilePermission()
                        .withIsEnableBackground(false)
                        .withIsEnableBluetooth(false)
                        .withIsEnableLocation(false)
                        .withIsEnableNotification(false))
        );
    }

    @ParameterizedTest
    @MethodSource("provideUserMobilePermission")
    void shouldUpdateMobilePermissions_OK(final UpdateMobilePermission updateMobilePermission) {
        final var currentUserMobile = buildUserMobile();

        when(authsProvider.getCurrentUserId()).thenReturn(currentUserMobile.getUserId());
        when(userMobileStore.findByUserId(currentUserMobile.getUserId())).thenReturn(Optional.of(currentUserMobile));
        when(userMobileStore.save(argThat(mobileArg -> mobileArg.getUserId() == currentUserMobile.getUserId()))).thenReturn(currentUserMobile);

        final var actual = userMobileService.updateMobilePermissions(updateMobilePermission);

        assertEquals(updateMobilePermission.getIsEnableBluetooth(), actual.getIsEnableBluetooth());
        assertEquals(updateMobilePermission.getIsEnableBackground(), actual.getIsEnableBackground());

        verify(authsProvider).getCurrentUserId();
        verify(userMobileStore).findByUserId(currentUserMobile.getUserId());
        verify(userMobileStore).save(argThat(mobileArg -> mobileArg.getUserId() == currentUserMobile.getUserId()));
    }
}