package com.openwt.officetracking.persistent.device_configuration_value;

import com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.persistent.device_configuration_value.DeviceConfigurationValueEntityMapper.*;
import static org.apache.commons.collections4.IterableUtils.toList;

@Repository
@RequiredArgsConstructor
public class DeviceConfigurationValueStore {

    private final DeviceConfigurationValueRepository deviceConfigurationValueRepository;

    public List<DeviceConfigurationValue> findAll() {
        return toDeviceConfigurationValues(toList(deviceConfigurationValueRepository.findAll()));
    }

    public Optional<DeviceConfigurationValue> findById(final UUID id) {
        return deviceConfigurationValueRepository.findById(id)
                .map(DeviceConfigurationValueEntityMapper::toDeviceConfigurationValue);
    }

    public DeviceConfigurationValue save(final DeviceConfigurationValue deviceConfigurationValue) {
        return toDeviceConfigurationValue(deviceConfigurationValueRepository.save(toDeviceConfigurationValueEntity(deviceConfigurationValue)));
    }

    public void delete(final UUID valueId) {
        deviceConfigurationValueRepository.deleteById(valueId);
    }
}