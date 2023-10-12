package com.openwt.officetracking.persistent.device_configuration;

import com.openwt.officetracking.domain.device_configuration.DeviceConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.persistent.device_configuration.DeviceConfigurationEntityMapper.toDeviceConfigurations;
import static org.apache.commons.collections4.IterableUtils.toList;

@Repository
@RequiredArgsConstructor
public class DeviceConfigurationStore {

    private final DeviceConfigurationRepository deviceConfigurationRepository;

    public List<DeviceConfiguration> findAll() {
        return toDeviceConfigurations(toList(deviceConfigurationRepository.findAll()));
    }

    public List<DeviceConfiguration> findAllByTypeId(final UUID typeId) {
        return toDeviceConfigurations(toList(deviceConfigurationRepository.findAllByDeviceTypeId(typeId)));
    }

    public Optional<DeviceConfiguration> findById(final UUID id) {
        return deviceConfigurationRepository.findById(id)
                .map(DeviceConfigurationEntityMapper::toDeviceConfiguration);
    }
}