package com.openwt.officetracking.persistent.device_model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.DeviceModelFakes.buildDeviceModelEntities;
import static com.openwt.officetracking.fake.DeviceModelFakes.buildDeviceModelEntity;
import static com.openwt.officetracking.persistent.device_model.DeviceModelEntityMapper.toDeviceModel;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceModelStoreTest {

    @Mock
    private DeviceModelRepository deviceModelRepository;

    @InjectMocks
    private DeviceModelStore deviceModelStore;

    @Test
    void shouldFindAllByDeviceTypeId_OK() {
        final var deviceTypeId = randomUUID();
        final var modelList = buildDeviceModelEntities();

        when(deviceModelRepository.findByTypeId(deviceTypeId)).thenReturn(modelList);

        final var actual = deviceModelStore.findByTypeId(deviceTypeId);

        assertEquals(modelList.size(), actual.size());

        verify(deviceModelRepository).findByTypeId(deviceTypeId);
    }

    @Test
    void shouldById_OK() {
        final var model = buildDeviceModelEntity();

        when(deviceModelRepository.findById(model.getId())).thenReturn(Optional.of(model));

        final var actual = deviceModelStore.findById(model.getId()).get();

        assertEquals(model.getId(), actual.getId());
        assertEquals(model.getTypeId(), actual.getTypeId());
        assertEquals(model.getName(), actual.getName());

        verify(deviceModelRepository).findById(model.getId());
    }

    @Test
    void shouldFindByName_OK() {
        final var model = buildDeviceModelEntity();

        when(deviceModelRepository.findByNameAndTypeId(model.getName(), model.getTypeId())).thenReturn(Optional.of(model));

        final var actual = deviceModelStore.findByNameAndTypeId(toDeviceModel(model)).get();

        assertEquals(model.getId(), actual.getId());
        assertEquals(model.getTypeId(), actual.getTypeId());
        assertEquals(model.getName(), actual.getName());

        verify(deviceModelRepository).findByNameAndTypeId(model.getName(), model.getTypeId());
    }

    @Test
    void shouldSave_OK() {
        final var model = buildDeviceModelEntity();

        when(deviceModelRepository.save(any(DeviceModelEntity.class))).thenReturn(model);

        final var actual = deviceModelStore.save(toDeviceModel(model));

        assertEquals(model.getId(), actual.getId());
        assertEquals(model.getTypeId(), actual.getTypeId());
        assertEquals(model.getName(), actual.getName());

        verify(deviceModelRepository).save(any(DeviceModelEntity.class));
    }
}