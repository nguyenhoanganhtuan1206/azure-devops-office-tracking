package com.openwt.officetracking.domain.device_configuration_value;

import com.openwt.officetracking.domain.device_model_configuration_value.DeviceModelConfigurationValueService;
import com.openwt.officetracking.persistent.device_configuration.DeviceConfigurationStore;
import com.openwt.officetracking.persistent.device_configuration_value.DeviceConfigurationValueStore;
import com.openwt.officetracking.persistent.device_model.DeviceModelStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.DeviceConfigurationFakes.buildDeviceConfiguration;
import static com.openwt.officetracking.fake.DeviceConfigurationValueFakes.*;
import static com.openwt.officetracking.fake.DeviceModelConfigurationValueFakes.buildDeviceModelConfigurationValue;
import static com.openwt.officetracking.fake.DeviceModelConfigurationValueFakes.buildDeviceModelConfigurationValues;
import static com.openwt.officetracking.fake.DeviceModelFakes.buildDeviceModel;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceConfigurationValueServiceTest {

    @Mock
    private DeviceConfigurationValueStore deviceConfigurationValueStore;

    @Mock
    private DeviceModelStore deviceModelStore;

    @Mock
    private DeviceConfigurationStore deviceConfigurationStore;

    @Mock
    private DeviceModelConfigurationValueService deviceModelConfigurationValueService;

    @InjectMocks
    private DeviceConfigurationValueService deviceConfigurationValueService;

    @Test
    void shouldFindById_OK() {
        final var deviceConfigurationValue = buildDeviceConfigurationValue();

        when(deviceConfigurationValueStore.findById(deviceConfigurationValue.getId()))
                .thenReturn(Optional.of(deviceConfigurationValue));

        final var actual = deviceConfigurationValueService.findById(deviceConfigurationValue.getId());

        assertEquals(deviceConfigurationValue, actual);
        assertEquals(deviceConfigurationValue.getId(), actual.getId());
        assertEquals(deviceConfigurationValue.getValue(), actual.getValue());

        verify(deviceConfigurationValueStore).findById(deviceConfigurationValue.getId());
    }

    @Test
    void shouldFindAllByModelAndDeviceConfiguration_OK() {
        final var deviceModelId = randomUUID();
        final var deviceConfigurationId = randomUUID();
        final var deviceConfigurationValue = buildDeviceConfigurationValue();
        final var expectedDeviceConfigurationValues = buildDeviceConfigurationValues();
        final var deviceModelConfigurationValues = buildDeviceModelConfigurationValues();

        when(deviceConfigurationValueStore.findById(any())).thenReturn(Optional.ofNullable(deviceConfigurationValue));
        when(deviceModelConfigurationValueService.findByModelAndConfiguration(deviceModelId, deviceConfigurationId)).thenReturn(deviceModelConfigurationValues);

        final var actual = deviceConfigurationValueService.findAllByModelAndDeviceConfiguration(deviceModelId, deviceConfigurationId);

        assertEquals(expectedDeviceConfigurationValues.size(), actual.size());
    }

    @Test
    void shouldCreate_OK() {
        final var valueId = randomUUID();
        final var model = buildDeviceModel();
        final var deviceConfigurationValue = buildDeviceConfigurationValue()
                .withId(valueId)
                .withModelId(model.getId());
        final var deviceConfigurationValueRequest = buildDeviceConfigurationValueRequest()
                .withId(valueId)
                .withModelId(model.getId());
        final var deviceModelConfigurationValue = buildDeviceModelConfigurationValue();
        final var deviceConfiguration = buildDeviceConfiguration();

        when(deviceModelStore.findById(any())).thenReturn(Optional.of(model));
        when(deviceConfigurationStore.findById(any())).thenReturn(Optional.of(deviceConfiguration));
        when(deviceModelConfigurationValueService.save(any())).thenReturn(deviceModelConfigurationValue);
        when(deviceConfigurationValueStore.save(any())).thenReturn(deviceConfigurationValue);

        final var actual = deviceConfigurationValueService.create(deviceConfigurationValueRequest);

        assertEquals(deviceConfigurationValue.getId(), actual.getId());
        assertEquals(deviceConfigurationValue.getModelId(), actual.getModelId());
        assertEquals(deviceConfigurationValue.getValue(), actual.getValue());

        verify(deviceModelStore).findById(any());
        verify(deviceConfigurationStore).findById(any());
        verify(deviceModelConfigurationValueService, times(1)).save(any());
        verify(deviceConfigurationValueStore).save(any());
    }

    @Test
    void shouldUpdate_OK() {
        final var valueId = randomUUID();
        final var model = buildDeviceModel();
        final var deviceConfigurationValue = buildDeviceConfigurationValue()
                .withId(valueId)
                .withModelId(model.getId());
        final var deviceConfigurationValueRequest = buildDeviceConfigurationValueRequest()
                .withId(valueId)
                .withModelId(model.getId());
        final var deviceModelConfigurationValue = buildDeviceModelConfigurationValue();
        final var deviceModelConfigurationValues = buildDeviceModelConfigurationValues();
        final var deviceConfiguration = buildDeviceConfiguration();


        when(deviceModelStore.findById(any())).thenReturn(Optional.of(model));
        when(deviceConfigurationValueStore.findById(any())).thenReturn(Optional.ofNullable(deviceConfigurationValue));
        when(deviceConfigurationStore.findById(any())).thenReturn(Optional.of(deviceConfiguration));
        when(deviceModelConfigurationValueService.findByValueId(any())).thenReturn(deviceModelConfigurationValue);
        when(deviceModelConfigurationValueService.findByModelAndConfiguration(any(), any())).thenReturn(deviceModelConfigurationValues);
        when(deviceConfigurationValueStore.save(any())).thenReturn(deviceConfigurationValue);

        final var actual = deviceConfigurationValueService.update(valueId, deviceConfigurationValueRequest);

        assertEquals(deviceConfigurationValue.getId(), actual.getId());

        verify(deviceModelStore).findById(any());
        verify(deviceConfigurationStore).findById(any());
        verify(deviceConfigurationValueStore, times(1)).save(any());
        verify(deviceModelConfigurationValueService, times(1)).findByValueId(any());
    }
}