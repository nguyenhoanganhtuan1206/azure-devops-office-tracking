package com.openwt.officetracking.persistent.device_configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.DeviceConfigurationFakes.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceConfigurationStoreTest {

    @Mock
    private DeviceConfigurationRepository deviceConfigurationRepository;

    @InjectMocks
    private DeviceConfigurationStore deviceConfigurationStore;

    @Test
    void shouldFindAll_OK() {
        final var expectedDeviceConfigurationEntities = buildDeviceConfigurationEntities();
        final var expectedDeviceConfigurations = buildDeviceConfigurations();

        when(deviceConfigurationRepository.findAll()).thenReturn(expectedDeviceConfigurationEntities);

       final var actual = deviceConfigurationStore.findAll();

        assertEquals(expectedDeviceConfigurations.size(), actual.size());

        verify(deviceConfigurationRepository).findAll();
    }

    @Test
    void shouldFindById_OK() {
        final var deviceConfiguration = buildDeviceConfigurationEntity();
        final var deviceConfigurationOptional = Optional.of(deviceConfiguration);

        when(deviceConfigurationRepository.findById(deviceConfiguration.getId()))
                .thenReturn(Optional.of(deviceConfiguration));

        final var expected = deviceConfigurationOptional.get();
        final var actual = deviceConfigurationStore.findById(deviceConfiguration.getId()).get();

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getLabel(), actual.getLabel());

        verify(deviceConfigurationRepository).findById(deviceConfiguration.getId());
    }
}