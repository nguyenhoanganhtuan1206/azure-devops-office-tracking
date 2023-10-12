package com.openwt.officetracking.persistent.device_device_configuration;

import com.openwt.officetracking.domain.device_device_configuration.DeviceDeviceConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.persistent.device_device_configuration.DeviceDeviceConfigurationEntityMapper.*;

@Repository
@RequiredArgsConstructor
public class DeviceDeviceConfigurationStore {

    private final DeviceDeviceConfigurationRepository deviceDeviceConfigurationRepository;

    public Optional<DeviceDeviceConfiguration> findById(final UUID id) {
        return deviceDeviceConfigurationRepository.findById(id)
                .map(DeviceDeviceConfigurationEntityMapper::toDeviceDeviceConfiguration);
    }

    public DeviceDeviceConfiguration save(final DeviceDeviceConfiguration courseAssignment) {
        return toDeviceDeviceConfiguration(deviceDeviceConfigurationRepository.save(toDeviceDeviceConfigurationEntity(courseAssignment)));
    }

    public List<DeviceDeviceConfiguration> findByDeviceId(final UUID courseId) {
        return toDeviceDeviceConfigurations(deviceDeviceConfigurationRepository.findByDeviceId(courseId));
    }

    public List<DeviceDeviceConfiguration> findByModelConfigurationValueId(final UUID modelConfigurationValueId) {
        return toDeviceDeviceConfigurations(deviceDeviceConfigurationRepository.findByDeviceModelConfigurationValueId(modelConfigurationValueId));
    }

    public void deleteByDeviceId(final UUID deviceId) {
        deviceDeviceConfigurationRepository.deleteByDeviceId(deviceId);
    }
}