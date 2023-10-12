package com.openwt.officetracking.domain.device_model_configuration_value;

import com.openwt.officetracking.persistent.device_model_configuration_value.DeviceModelConfigurationValueStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.DeviceModelConfigurationValueFakes.buildDeviceModelConfigurationValue;
import static com.openwt.officetracking.fake.DeviceModelConfigurationValueFakes.buildDeviceModelConfigurationValues;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceModelConfigurationValueServiceTest {

    @Mock
    private DeviceModelConfigurationValueStore deviceModelConfigurationValueStore;

    @InjectMocks
    private  DeviceModelConfigurationValueService deviceModelConfigurationValueService;

    @Test
    void shouldFindById_OK() {
        final var deviceModelConfigurationValue = buildDeviceModelConfigurationValue();

        when(deviceModelConfigurationValueStore.findById(deviceModelConfigurationValue.getId()))
                .thenReturn(Optional.of(deviceModelConfigurationValue));

        final var actual = deviceModelConfigurationValueService.findById(deviceModelConfigurationValue.getId());

        assertEquals(deviceModelConfigurationValue.getId(), actual.getId());
        assertEquals(deviceModelConfigurationValue.getDeviceModelId(), actual.getDeviceModelId());
        assertEquals(deviceModelConfigurationValue.getDeviceConfigurationId(), actual.getDeviceConfigurationId());
        assertEquals(deviceModelConfigurationValue.getDeviceConfigurationValueId(), actual.getDeviceConfigurationValueId());

        verify(deviceModelConfigurationValueStore).findById(deviceModelConfigurationValue.getId());
    }

    @Test
    public void shouldFindByModelAndConfigurationAndValue_OK() {
        final var modelId = randomUUID();
        final var configurationId = randomUUID();
        final var valueId = randomUUID();
        final var deviceModelConfigurationValue = buildDeviceModelConfigurationValue();

        when(deviceModelConfigurationValueStore.findByDeviceModelAndDeviceConfigurationAndDeviceConfigurationValue(modelId, configurationId, valueId))
                .thenReturn(Optional.of(deviceModelConfigurationValue));

        final var actual = deviceModelConfigurationValueService.findByModelAndConfigurationAndValue(modelId, configurationId, valueId);

        assertEquals(deviceModelConfigurationValue.getId(), actual.getId());
        assertEquals(deviceModelConfigurationValue.getDeviceModelId(), actual.getDeviceModelId());
        assertEquals(deviceModelConfigurationValue.getDeviceConfigurationId(), actual.getDeviceConfigurationId());
        assertEquals(deviceModelConfigurationValue.getDeviceConfigurationValueId(), actual.getDeviceConfigurationValueId());

        verify(deviceModelConfigurationValueStore).findByDeviceModelAndDeviceConfigurationAndDeviceConfigurationValue(modelId, configurationId, valueId);
    }

    @Test
    public void shouldFindByModels_OK() {
        final var modelId = randomUUID();
        final var deviceModelConfigurationValues = buildDeviceModelConfigurationValues();

        when(deviceModelConfigurationValueStore.findByDeviceModelId(modelId)).thenReturn(deviceModelConfigurationValues);

        final var actual = deviceModelConfigurationValueService.findByModel(modelId);

        assertEquals(deviceModelConfigurationValues.size(), actual.size());
        assertEquals(deviceModelConfigurationValues.get(0).getId(), actual.get(0).getId());
        assertEquals(deviceModelConfigurationValues.get(0).getDeviceModelId(), actual.get(0).getDeviceModelId());
        assertEquals(deviceModelConfigurationValues.get(0).getDeviceConfigurationId(), actual.get(0).getDeviceConfigurationId());
        assertEquals(deviceModelConfigurationValues.get(0).getDeviceConfigurationValueId(), actual.get(0).getDeviceConfigurationValueId());

        verify(deviceModelConfigurationValueStore).findByDeviceModelId(modelId);
    }

    @Test
    public void shouldFindByModelAndConfiguration_OK() {
        final var modelId = randomUUID();
        final var configurationId = randomUUID();
        final var deviceModelConfigurationValues = buildDeviceModelConfigurationValues();

        when(deviceModelConfigurationValueStore.findByDeviceModelAndDeviceConfiguration(modelId, configurationId)).thenReturn(deviceModelConfigurationValues);

        final var actual = deviceModelConfigurationValueService.findByModelAndConfiguration(modelId, configurationId);

        assertEquals(deviceModelConfigurationValues.size(), actual.size());
        assertEquals(deviceModelConfigurationValues.get(0).getId(), actual.get(0).getId());
        assertEquals(deviceModelConfigurationValues.get(0).getDeviceModelId(), actual.get(0).getDeviceModelId());
        assertEquals(deviceModelConfigurationValues.get(0).getDeviceConfigurationId(), actual.get(0).getDeviceConfigurationId());
        assertEquals(deviceModelConfigurationValues.get(0).getDeviceConfigurationValueId(), actual.get(0).getDeviceConfigurationValueId());

        verify(deviceModelConfigurationValueStore).findByDeviceModelAndDeviceConfiguration(modelId, configurationId);
    }
}