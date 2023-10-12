package com.openwt.officetracking.persistent.device_device_configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.openwt.officetracking.fake.DeviceDeviceConfigurationFakes.buildDeviceDeviceConfigurationEntity;
import static com.openwt.officetracking.persistent.device_device_configuration.DeviceDeviceConfigurationEntityMapper.toDeviceDeviceConfiguration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceDeviceConfigurationStoreTest {

    @Mock
    private DeviceDeviceConfigurationRepository deviceDeviceConfigurationRepository;

    @InjectMocks
    private DeviceDeviceConfigurationStore deviceDeviceConfigurationStore;

    @Test
    void shouldSave_OK() {
        final var deviceDeviceConfiguration = buildDeviceDeviceConfigurationEntity();

        when(deviceDeviceConfigurationRepository.save(any(DeviceDeviceConfigurationEntity.class))).thenReturn(deviceDeviceConfiguration);

        final var actual = deviceDeviceConfigurationStore.save(toDeviceDeviceConfiguration(deviceDeviceConfiguration));

        assertEquals(deviceDeviceConfiguration.getId(), actual.getId());
        assertEquals(deviceDeviceConfiguration.getDeviceId(), actual.getDeviceId());
        assertEquals(deviceDeviceConfiguration.getDeviceModelConfigurationValueId(), actual.getDeviceModelConfigurationValueId());

        verify(deviceDeviceConfigurationRepository).save(any(DeviceDeviceConfigurationEntity.class));
    }
}