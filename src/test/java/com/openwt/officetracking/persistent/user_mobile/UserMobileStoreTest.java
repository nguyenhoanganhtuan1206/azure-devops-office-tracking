package com.openwt.officetracking.persistent.user_mobile;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.UserMobileFakes.buildUserMobileEntities;
import static com.openwt.officetracking.fake.UserMobileFakes.buildUserMobileEntity;
import static com.openwt.officetracking.persistent.user_mobile.UserMobileEntityMapper.toUserMobile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserMobileStoreTest {

    @Mock
    private UserMobileRepository userMobileRepository;

    @InjectMocks
    private UserMobileStore userMobileStore;

    @Test
    void findByBiometryToken() {
        final var userMobile = buildUserMobileEntity();

        when(userMobileRepository.findByBiometryToken(userMobile.getBiometryToken()))
                .thenReturn(Optional.of(userMobile));

        final var actual = userMobileStore.findByBiometryToken(userMobile.getBiometryToken()).get();

        assertEquals(userMobile.getId(), actual.getId());
        assertEquals(userMobile.getUserId(), actual.getUserId());
        assertEquals(userMobile.getDeviceType(), actual.getDeviceType());
        assertEquals(userMobile.getModel(), actual.getModel());
        assertEquals(userMobile.getOsVersion(), actual.getOsVersion());
        assertEquals(userMobile.getBiometryToken(), actual.getBiometryToken());
        assertEquals(userMobile.getFcmToken(), actual.getFcmToken());
        assertEquals(userMobile.isActive(), actual.isActive());
        assertEquals(userMobile.getRegisteredAt(), actual.getRegisteredAt());

        verify(userMobileRepository).findByBiometryToken(userMobile.getBiometryToken());
    }

    @Test
    void shouldFindAllUserDeviceInformation_OK() {
        final var userDevices = buildUserMobileEntities();

        when(userMobileRepository.findAll()).thenReturn(userDevices);

        final var actual = userMobileStore.findAll();

        assertEquals(userDevices.size(), actual.size());

        verify(userMobileRepository).findAll();
    }

    @Test
    void shouldFindByUserId_OK() {
        final var userDevice = buildUserMobileEntity();

        when(userMobileRepository.findByUserId(userDevice.getUserId())).thenReturn(Optional.of(userDevice));

        final var actual = userMobileStore.findByUserId(userDevice.getUserId()).get();

        assertEquals(userDevice.getId(), actual.getId());
        assertEquals(userDevice.getUserId(), actual.getUserId());
        assertEquals(userDevice.getDeviceType(), actual.getDeviceType());
        assertEquals(userDevice.getModel(), actual.getModel());
        assertEquals(userDevice.getOsVersion(), actual.getOsVersion());
        assertEquals(userDevice.getBiometryToken(), actual.getBiometryToken());
        assertEquals(userDevice.getFcmToken(), actual.getFcmToken());
        assertEquals(userDevice.getRegisteredAt(), actual.getRegisteredAt());

        verify(userMobileRepository).findByUserId(userDevice.getUserId());
    }

    @Test
    void shouldSaveUserMobile_OK() {
        final var userDevice = buildUserMobileEntity();

        when(userMobileRepository.save(any(UserMobileEntity.class))).thenReturn(userDevice);

        final var actual = userMobileStore.save(toUserMobile(userDevice));

        assertEquals(userDevice.getId(), actual.getId());
        assertEquals(userDevice.getUserId(), actual.getUserId());
        assertEquals(userDevice.getDeviceType(), actual.getDeviceType());
        assertEquals(userDevice.getModel(), actual.getModel());
        assertEquals(userDevice.getOsVersion(), actual.getOsVersion());
        assertEquals(userDevice.getBiometryToken(), actual.getBiometryToken());
        assertEquals(userDevice.getFcmToken(), actual.getFcmToken());
        assertEquals(userDevice.getRegisteredAt(), actual.getRegisteredAt());
        assertEquals(userDevice.isActive(), actual.isActive());

        verify(userMobileRepository).save(any(UserMobileEntity.class));
    }

    @Test
    void shouldFindById_OK() {
        final var userDevice = buildUserMobileEntity();

        when(userMobileRepository.findById(userDevice.getUserId())).thenReturn(Optional.of(userDevice));

        final var actual = userMobileStore.findById(userDevice.getUserId()).get();

        assertEquals(userDevice.getId(), actual.getId());
        assertEquals(userDevice.getUserId(), actual.getUserId());
        assertEquals(userDevice.getDeviceType(), actual.getDeviceType());
        assertEquals(userDevice.getModel(), actual.getModel());
        assertEquals(userDevice.getOsVersion(), actual.getOsVersion());
        assertEquals(userDevice.getBiometryToken(), actual.getBiometryToken());
        assertEquals(userDevice.getFcmToken(), actual.getFcmToken());
        assertEquals(userDevice.getRegisteredAt(), actual.getRegisteredAt());

        verify(userMobileRepository).findById(userDevice.getUserId());
    }
}