package com.openwt.officetracking.persistent.device_model_configuration_value;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.DeviceModelConfigurationValueFakes.buildDeviceModelConfigurationValueEntities;
import static com.openwt.officetracking.fake.DeviceModelConfigurationValueFakes.buildDeviceModelConfigurationValueEntity;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceModelConfigurationValueStoreTest {

    @Mock
    private DeviceModelConfigurationValueRepository deviceModelConfigurationValueRepository;

    @InjectMocks
    private DeviceModelConfigurationValueStore deviceModelConfigurationValueStore;

    @Test
    void shouldById_OK() {
        final var modelConfigurationValue = buildDeviceModelConfigurationValueEntity();

        when(deviceModelConfigurationValueRepository.findById(modelConfigurationValue.getId())).thenReturn(Optional.of(modelConfigurationValue));

        final var actual = deviceModelConfigurationValueStore.findById(modelConfigurationValue.getId()).get();

        assertEquals(modelConfigurationValue.getId(), actual.getId());
        assertEquals(modelConfigurationValue.getDeviceModelId(), actual.getDeviceModelId());
        assertEquals(modelConfigurationValue.getDeviceConfigurationId(), actual.getDeviceConfigurationId());
        assertEquals(modelConfigurationValue.getDeviceConfigurationValueId(), actual.getDeviceConfigurationValueId());

        verify(deviceModelConfigurationValueRepository).findById(modelConfigurationValue.getId());
    }

    @Test
    void shouldFindByDeviceModelId_OK() {
        final var modelId = randomUUID();
        final var modelConfigurationValues = buildDeviceModelConfigurationValueEntities();

        when(deviceModelConfigurationValueRepository.findByDeviceModelId(modelId)).thenReturn(modelConfigurationValues);

        final var actual = deviceModelConfigurationValueStore.findByDeviceModelId(modelId);

        assertEquals(modelConfigurationValues.size(), actual.size());

        verify(deviceModelConfigurationValueRepository).findByDeviceModelId(modelId);
    }

    @Test
    void shouldFindDeviceModelAndDeviceConfiguration_OK() {
        final var modelId = randomUUID();
        final var configurationId = randomUUID();
        final var modelConfigurationValues = buildDeviceModelConfigurationValueEntities();

        when(deviceModelConfigurationValueRepository.findByDeviceModelIdAndDeviceConfigurationId(modelId, configurationId)).thenReturn(modelConfigurationValues);

        final var actual = deviceModelConfigurationValueStore.findByDeviceModelAndDeviceConfiguration(modelId, configurationId);

        assertEquals(modelConfigurationValues.size(), actual.size());

        verify(deviceModelConfigurationValueRepository).findByDeviceModelIdAndDeviceConfigurationId(modelId, configurationId);
    }

    @Test
    void shouldFindDeviceModelAndDeviceConfigurationAndDeviceConfigurationValue_OK() {
        final var modelId = randomUUID();
        final var configurationId = randomUUID();
        final var valueId = randomUUID();
        final var modelConfigurationValue = buildDeviceModelConfigurationValueEntity();

        when(deviceModelConfigurationValueRepository.findByDeviceModelIdAndDeviceConfigurationIdAndDeviceConfigurationValueId(modelId, configurationId, valueId)).thenReturn(Optional.ofNullable(modelConfigurationValue));

        final var actual = deviceModelConfigurationValueStore.findByDeviceModelAndDeviceConfigurationAndDeviceConfigurationValue(modelId, configurationId, valueId).get();

        assertEquals(modelConfigurationValue.getId(), actual.getId());
        assertEquals(modelConfigurationValue.getDeviceModelId(), actual.getDeviceModelId());
        assertEquals(modelConfigurationValue.getDeviceConfigurationId(), actual.getDeviceConfigurationId());
        assertEquals(modelConfigurationValue.getDeviceConfigurationValueId(), actual.getDeviceConfigurationValueId());

        verify(deviceModelConfigurationValueRepository).findByDeviceModelIdAndDeviceConfigurationIdAndDeviceConfigurationValueId(modelId, configurationId, valueId);
    }
}