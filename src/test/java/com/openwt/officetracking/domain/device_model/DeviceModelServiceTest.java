package com.openwt.officetracking.domain.device_model;

import com.openwt.officetracking.domain.device_configuration.DeviceConfigurationService;
import com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValueService;
import com.openwt.officetracking.domain.device_model_configuration_value.DeviceModelConfigurationValueService;
import com.openwt.officetracking.error.BadRequestException;
import com.openwt.officetracking.error.NotFoundException;
import com.openwt.officetracking.persistent.device_device_configuration.DeviceDeviceConfigurationStore;
import com.openwt.officetracking.persistent.device_model.DeviceModelStore;
import com.openwt.officetracking.persistent.device_model_configuration_value.DeviceModelConfigurationValueStore;
import com.openwt.officetracking.persistent.device_type.DeviceTypeStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.DeviceModelFakes.buildDeviceModel;
import static com.openwt.officetracking.fake.DeviceTypeFakes.buildDeviceType;
import static java.util.Collections.emptyList;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceModelServiceTest {

    @Mock
    private DeviceModelStore deviceModelStore;

    @Mock
    private DeviceTypeStore deviceTypeStore;

    @Mock
    private DeviceModelConfigurationValueService deviceModelConfigurationValueService;

    @Mock
    private DeviceDeviceConfigurationStore deviceDeviceConfigurationStore;

    @Mock
    private DeviceModelConfigurationValueStore deviceModelConfigurationValueStore;


    @Mock
    private DeviceConfigurationService deviceConfigurationService;

    @Mock
    private DeviceConfigurationValueService deviceConfigurationValueService;

    @InjectMocks
    private DeviceModelService deviceModelService;

    @Test
    void shouldFindId_OK() {
        final var model = buildDeviceModel();

        when(deviceModelStore.findById(model.getId())).thenReturn(Optional.of(model));

        final var actual = deviceModelService.findById(model.getId());

        assertEquals(model.getId(), actual.getId());
        assertEquals(model.getTypeId(), actual.getTypeId());
        assertEquals(model.getName(), actual.getName());

        verify(deviceModelStore).findById(model.getId());
    }

    @Test
    void shouldFindId_ThrowNotFound() {
        final var model = buildDeviceModel();

        when(deviceModelStore.findById(model.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> deviceModelService.findById(model.getId()));

        verify(deviceModelStore).findById(model.getId());
    }

    @Test
    void shouldCreateModel_OK() {
        final var type = buildDeviceType();
        final var model = buildDeviceModel()
                .withTypeId(type.getId());

        when(deviceModelStore.findByNameAndTypeId(model)).thenReturn(Optional.empty());
        when(deviceTypeStore.findById(any())).thenReturn(Optional.of(type));
        when(deviceModelStore.save(argThat(modelArg -> modelArg.getId() == model.getId()))).thenReturn(model);

        final var actual = deviceModelService.create(model);

        assertEquals(model.getId(), actual.getId());
        assertEquals(model.getTypeId(), actual.getTypeId());
        assertEquals(model.getName(), actual.getName());

        verify(deviceModelStore).findByNameAndTypeId(model);
        verify(deviceModelStore).save(argThat(modelArg -> modelArg.getId() == model.getId()));
    }

    @Test
    void shouldCreateModel_ThrowModelNameExisted() {
        final var type = buildDeviceType();
        final var model = buildDeviceModel()
                .withTypeId(type.getId());

        when(deviceModelStore.findByNameAndTypeId(model)).thenReturn(Optional.of(model));
        when(deviceTypeStore.findById(any())).thenReturn(Optional.of(type));

        assertThrows(BadRequestException.class, () -> deviceModelService.create(model));

        verify(deviceModelStore).findByNameAndTypeId(model);
    }

    @Test
    void shouldUpdateModel_OK() {
        final var modelId = randomUUID();
        final var modelRequest = buildDeviceModel()
                .withId(modelId);
        final var currentModel = buildDeviceModel()
                .withId(modelId);

        when(deviceModelStore.findById(currentModel.getId())).thenReturn(Optional.of(currentModel));
        when(deviceTypeStore.findById(any())).thenReturn(Optional.of(buildDeviceType()));
        when(deviceModelStore.save(any())).thenReturn(modelRequest);

        final DeviceModel actual = deviceModelService.update(modelId, modelRequest);

        assertEquals(modelRequest.getId(), actual.getId());
        assertEquals(modelRequest.getTypeId(), actual.getTypeId());
        assertEquals(modelRequest.getName(), actual.getName());

        verify(deviceModelStore).findById(currentModel.getId());
        verify(deviceModelStore).save(any());
    }

    @Test
    void shouldDeleteModel_OK() {
        final var modelId = randomUUID();
        final var currentModel = buildDeviceModel()
                .withId(modelId);

        when(deviceModelStore.findById(modelId)).thenReturn(Optional.of(currentModel));
        when(deviceModelConfigurationValueService.findByModel(any())).thenReturn(emptyList());

        deviceModelService.delete(modelId);

        verify(deviceModelStore).findById(modelId);
        verify(deviceModelConfigurationValueService).findByModel(any());
        verify(deviceModelConfigurationValueService).deleteByDeviceModel(modelId);
        verify(deviceModelStore).delete(modelId);
    }
}