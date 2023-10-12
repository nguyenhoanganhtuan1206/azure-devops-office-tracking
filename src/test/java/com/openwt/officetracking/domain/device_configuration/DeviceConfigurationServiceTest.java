package com.openwt.officetracking.domain.device_configuration;

import com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValueService;
import com.openwt.officetracking.domain.device_model_configuration_value.DeviceModelConfigurationValueService;
import com.openwt.officetracking.persistent.device_configuration.DeviceConfigurationStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.DeviceConfigurationFakes.buildDeviceConfiguration;
import static com.openwt.officetracking.fake.DeviceConfigurationFakes.buildDeviceConfigurations;
import static com.openwt.officetracking.fake.DeviceConfigurationValueFakes.buildDeviceConfigurationValues;
import static com.openwt.officetracking.fake.DeviceModelConfigurationValueFakes.buildDeviceModelConfigurationValues;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceConfigurationServiceTest {

    @Mock
    private DeviceConfigurationStore deviceConfigurationStore;

    @Mock
    private DeviceConfigurationValueService deviceConfigurationValueService;

    @Mock
    private DeviceModelConfigurationValueService deviceModelConfigurationValueService;

    @InjectMocks
    private DeviceConfigurationService deviceConfigurationService;

    @Test
    void shouldFindById_OK() {
        final var deviceConfiguration = buildDeviceConfiguration();

        when(deviceConfigurationStore.findById(deviceConfiguration.getId()))
                .thenReturn(Optional.of(deviceConfiguration));

        final var actual = deviceConfigurationService.findById(deviceConfiguration.getId());

        assertEquals(deviceConfiguration, actual);
        assertEquals(deviceConfiguration.getId(), actual.getId());
        assertEquals(deviceConfiguration.getLabel(), actual.getLabel());

        verify(deviceConfigurationStore).findById(deviceConfiguration.getId());
    }

    @Test
    void shouldFindAllByModelId_OK() {
        final var deviceModelId = randomUUID();
        final var deviceConfigurationId = randomUUID();
        final var expectedDeviceConfigurationValues = buildDeviceConfigurationValues();
        final var deviceConfiguration = buildDeviceConfiguration()
                .withId(deviceConfigurationId)
                .withValues(expectedDeviceConfigurationValues);
        final var expectedDeviceConfigurations = buildDeviceConfigurations();
        final var expectedModelDeviceConfigurationValues = buildDeviceModelConfigurationValues();

        when(deviceModelConfigurationValueService.findByModel(deviceModelId)).thenReturn(expectedModelDeviceConfigurationValues);
        when(deviceConfigurationStore.findById(any())).thenReturn(Optional.of(deviceConfiguration));
        when(deviceConfigurationValueService.findAllByModelAndDeviceConfiguration(deviceModelId, deviceConfiguration.getId()))
                .thenReturn(expectedDeviceConfigurationValues);

        final var actual = deviceConfigurationService.findAllByModelId(deviceModelId);

        assertEquals(expectedDeviceConfigurations.size(), actual.size());

        verify(deviceModelConfigurationValueService).findByModel(deviceModelId);
    }
}