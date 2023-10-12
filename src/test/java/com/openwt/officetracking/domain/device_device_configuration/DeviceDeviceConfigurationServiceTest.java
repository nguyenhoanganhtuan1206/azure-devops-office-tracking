package com.openwt.officetracking.domain.device_device_configuration;

import com.openwt.officetracking.persistent.device_device_configuration.DeviceDeviceConfigurationStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.openwt.officetracking.fake.DeviceDeviceConfigurationFakes.buildDeviceDeviceConfiguration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceDeviceConfigurationServiceTest {

    @Mock
    private DeviceDeviceConfigurationStore deviceDeviceConfigurationStore;

    @InjectMocks
    private DeviceDeviceConfigurationService deviceDeviceConfigurationService;

    @Test
    void shouldCreate_OK() {
        final var deviceDeviceConfiguration = buildDeviceDeviceConfiguration();

        when(deviceDeviceConfigurationStore.save(argThat(deviceConfigurationArg -> deviceConfigurationArg.getId().equals(deviceDeviceConfiguration.getId())))).thenReturn(deviceDeviceConfiguration);

        final var actual = deviceDeviceConfigurationService.create(deviceDeviceConfiguration);

        assertEquals(deviceDeviceConfiguration.getId(), actual.getId());
        assertEquals(deviceDeviceConfiguration.getDeviceId(), actual.getDeviceId());
        assertEquals(deviceDeviceConfiguration.getDeviceModelConfigurationValueId(), actual.getDeviceModelConfigurationValueId());

        verify(deviceDeviceConfigurationStore).save(argThat(deviceConfigurationArg -> deviceConfigurationArg.getId().equals(deviceDeviceConfiguration.getId())));
    }
}