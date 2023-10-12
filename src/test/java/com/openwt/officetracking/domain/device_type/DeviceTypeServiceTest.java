package com.openwt.officetracking.domain.device_type;

import com.openwt.officetracking.domain.device_model.DeviceModelService;
import com.openwt.officetracking.persistent.device_type.DeviceTypeStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.DeviceTypeFakes.buildDeviceType;
import static com.openwt.officetracking.fake.DeviceTypeFakes.buildDeviceTypeResponse;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceTypeServiceTest {

    @Mock
    private DeviceTypeStore deviceTypeStore;

    @Mock
    private DeviceModelService deviceModelService;

    @InjectMocks
    private DeviceTypeService deviceTypeService;

    @Test
    void shouldFindDeviceTypeById_OK() {
        final var deviceTypeId = randomUUID();
        final var deviceType = buildDeviceType()
                .withId(deviceTypeId);
        final var deviceTypeResponse = buildDeviceTypeResponse()
                .withId(deviceTypeId);

        when(deviceTypeStore.findById(deviceTypeResponse.getId())).thenReturn(Optional.ofNullable(deviceType));
        when(deviceModelService.findAllByTypeId(deviceTypeId)).thenReturn(deviceTypeResponse.getModels());

        final var actual = deviceTypeService.findDeviceTypeById(deviceTypeResponse.getId());

        assertEquals(deviceTypeResponse.getId(), actual.getId());
        assertEquals(deviceTypeResponse.getModels().size(), actual.getModels().size());

        verify(deviceTypeStore).findById(deviceTypeResponse.getId());
        verify(deviceModelService).findAllByTypeId(deviceTypeId);
    }

    @Test
    void shouldCreate_OK() {
        final var type = buildDeviceType();

        when(deviceTypeStore.save(type)).thenReturn(type);

        final var actual = deviceTypeService.create(type);

        assertEquals(type.getId(), actual.getId());
        assertEquals(type.getName(), actual.getName());

        verify(deviceTypeStore).save(type);
    }

    @Test
    void shouldUpdate_OK() {
        final var id = randomUUID();
        final var type = buildDeviceType()
                .withId(id);

        when(deviceTypeStore.save(type)).thenReturn(type);

        final var actual = deviceTypeService.create(type);

        assertEquals(type.getId(), actual.getId());
        assertEquals(type.getName(), actual.getName());

        verify(deviceTypeStore).save(type);
    }

    @Test
    void shouldDelete_OK() {
        final var typeId = randomUUID();
        final var currentModel = buildDeviceType()
                .withId(typeId);

        when(deviceTypeStore.findById(typeId)).thenReturn(Optional.of(currentModel));

        deviceTypeService.delete(typeId);

        verify(deviceTypeStore).findById(typeId);
    }
}