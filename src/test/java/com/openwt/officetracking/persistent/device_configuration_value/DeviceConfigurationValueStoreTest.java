package com.openwt.officetracking.persistent.device_configuration_value;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.DeviceConfigurationValueFakes.*;
import static com.openwt.officetracking.persistent.device_configuration_value.DeviceConfigurationValueEntityMapper.toDeviceConfigurationValue;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceConfigurationValueStoreTest {

    @Mock
    private DeviceConfigurationValueRepository deviceConfigurationValueRepository;

    @InjectMocks
    private DeviceConfigurationValueStore deviceConfigurationValueStore;

    @Test
    void shouldFindAll_OK() {
        final var expectedDeviceConfigurationValueEntities = buildDeviceConfigurationValueEntities();
        final var expectedDeviceConfigurationValues = buildDeviceConfigurationValues();

        when(deviceConfigurationValueRepository.findAll()).thenReturn(expectedDeviceConfigurationValueEntities);

        final var actual = deviceConfigurationValueStore.findAll();

        assertEquals(expectedDeviceConfigurationValues.size(), actual.size());

        verify(deviceConfigurationValueRepository).findAll();
    }

    @Test
    void shouldFindById_OK() {
        final var deviceConfigurationValue = buildDeviceConfigurationValueEntity();
        final var deviceConfigurationValueOptional = Optional.of(deviceConfigurationValue);

        when(deviceConfigurationValueRepository.findById(deviceConfigurationValue.getId()))
                .thenReturn(Optional.of(deviceConfigurationValue));

        final var expected = deviceConfigurationValueOptional.get();
        final var actual = deviceConfigurationValueStore.findById(deviceConfigurationValue.getId()).get();

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getValue(), actual.getValue());

        verify(deviceConfigurationValueRepository).findById(deviceConfigurationValue.getId());
    }

    @Test
    void shouldSave_OK() {
        final var value = buildDeviceConfigurationValueEntity();

        when(deviceConfigurationValueRepository.save(any(DeviceConfigurationValueEntity.class))).thenReturn(value);

        final var actual = deviceConfigurationValueStore.save(toDeviceConfigurationValue(value));

        assertEquals(value.getId(), actual.getId());
        assertEquals(value.getValue(), actual.getValue());

        verify(deviceConfigurationValueRepository).save(any(DeviceConfigurationValueEntity.class));
    }

    @Test
    public void shouldDelete_OK() {
        final var valueId = randomUUID();

        doNothing().when(deviceConfigurationValueRepository).deleteById(valueId);

        deviceConfigurationValueStore.delete(valueId);

        verify(deviceConfigurationValueRepository).deleteById(valueId);
    }
}