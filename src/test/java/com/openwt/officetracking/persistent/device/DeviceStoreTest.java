package com.openwt.officetracking.persistent.device;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.DeviceFakes.buildDeviceEntities;
import static com.openwt.officetracking.fake.DeviceFakes.buildDeviceEntity;
import static com.openwt.officetracking.persistent.device.DeviceEntityMapper.toDevice;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceStoreTest {

    @InjectMocks
    private DeviceStore deviceStore;

    @Mock
    private DeviceRepository deviceRepository;

    @Test
    void shouldFindById_OK() {
        final var device = buildDeviceEntity();
        final var deviceOptional = Optional.of(device);

        when(deviceRepository.findById(device.getId()))
                .thenReturn(Optional.of(device));

        final var expected = deviceOptional.get();
        final var actual = deviceStore.findById(device.getId()).get();

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getModelId(), actual.getModelId());
        assertEquals(expected.getDetail(), actual.getDetail());
        assertEquals(expected.getSerialNumber(), actual.getSerialNumber());
        assertEquals(expected.getPurchaseAt(), actual.getPurchaseAt());
        assertEquals(expected.getWarrantyEndAt(), actual.getWarrantyEndAt());
        assertEquals(expected.getDeviceStatus(), actual.getDeviceStatus());

        verify(deviceRepository).findById(device.getId());
    }

    @Test
    void shouldSave_OK() {
        final var device = buildDeviceEntity();

        when(deviceRepository.save(any(DeviceEntity.class)))
                .thenReturn(device);

        final var actual = deviceStore.save(toDevice(device));

        assertEquals(device.getId(), actual.getId());
        assertEquals(device.getModelId(), actual.getModelId());
        assertEquals(device.getSerialNumber(), actual.getSerialNumber());
        assertEquals(device.getAssignUserId(), actual.getAssignUserId());
        assertEquals(device.getPurchaseAt(), actual.getPurchaseAt());
        assertEquals(device.getWarrantyEndAt(), actual.getWarrantyEndAt());
        assertEquals(device.getNote(), actual.getRequestNote());
        assertEquals(device.getReason(), actual.getRequestReason());
        assertEquals(device.getRequestedAt(), actual.getRequestedAt());
        assertEquals(device.getAcceptedAt(), actual.getAcceptedAt());
        assertEquals(device.getRejectedAt(), actual.getRejectedAt());
        assertEquals(device.getCreatedAt(), actual.getCreatedAt());
        assertEquals(device.getLastModifiedAt(), actual.getLastModifiedAt());

        verify(deviceRepository).save(any(DeviceEntity.class));
    }

    @Test
    void shouldSerialNumber_OK() {
        final var device = buildDeviceEntity();
        final var deviceOptional = Optional.of(device);

        when(deviceRepository.findBySerialNumber(device.getSerialNumber()))
                .thenReturn(Optional.of(device));

        final var expected = deviceOptional.get();
        final var actual = deviceStore.findBySerialNumber(device.getSerialNumber()).get();

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getModelId(), actual.getModelId());
        assertEquals(expected.getSerialNumber(), actual.getSerialNumber());
        assertEquals(expected.getAssignUserId(), actual.getAssignUserId());
        assertEquals(expected.getPurchaseAt(), actual.getPurchaseAt());
        assertEquals(expected.getWarrantyEndAt(), actual.getWarrantyEndAt());
        assertEquals(expected.getNote(), actual.getRequestNote());
        assertEquals(expected.getReason(), actual.getRequestReason());
        assertEquals(expected.getRequestedAt(), actual.getRequestedAt());
        assertEquals(expected.getAcceptedAt(), actual.getAcceptedAt());
        assertEquals(expected.getRejectedAt(), actual.getRejectedAt());
        assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
        assertEquals(expected.getLastModifiedAt(), actual.getLastModifiedAt());

        verify(deviceRepository).findBySerialNumber(device.getSerialNumber());
    }

    @Test
    void shouldFindAllRequests_OK() {
        final var devices = buildDeviceEntities();

        when(deviceRepository.findAllRequests()).thenReturn(devices);

        final var actual = deviceStore.findAllRequests();

        assertEquals(devices.size(), actual.size());

        verify(deviceRepository).findAllRequests();
    }

    @Test
    void shouldFindRequestsByUser_OK() {
        final var currentUserId = randomUUID();
        final var devices = buildDeviceEntities();

        when(deviceRepository.findAllRequests(currentUserId)).thenReturn(devices);

        final var actual = deviceStore.findAllRequests(currentUserId);

        assertEquals(devices.size(), actual.size());

        verify(deviceRepository).findAllRequests(currentUserId);
    }

    @Test
    void shouldFindAllDevices_OK() {
        final var devices = buildDeviceEntities();

        when(deviceRepository.findAllDevices()).thenReturn(devices);

        final var actual = deviceStore.findAllDevices();

        assertEquals(devices.size(), actual.size());

        verify(deviceRepository).findAllDevices();
    }

    @Test
    void shouldFindByAssignUserIdIsNotRequest_OK() {
        final var userId = randomUUID();
        final var devices = buildDeviceEntities();

        when(deviceRepository.findMyDevices(userId)).thenReturn(devices);

        final var actual = deviceStore.findMyDevices(userId);

        assertEquals(devices.size(), actual.size());

        verify(deviceRepository).findMyDevices(userId);
    }
}
