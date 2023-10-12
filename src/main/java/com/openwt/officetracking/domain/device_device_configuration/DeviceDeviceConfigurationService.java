package com.openwt.officetracking.domain.device_device_configuration;

import com.openwt.officetracking.persistent.device_device_configuration.DeviceDeviceConfigurationStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.domain.device_device_configuration.DeviceDeviceConfigurationError.supplyDeviceDeviceConfigurationNotFound;


@Service
@RequiredArgsConstructor
public class DeviceDeviceConfigurationService {

    private final DeviceDeviceConfigurationStore deviceDeviceConfigurationStore;

    public DeviceDeviceConfiguration findById(final UUID id) {
        return deviceDeviceConfigurationStore.findById(id)
                .orElseThrow(supplyDeviceDeviceConfigurationNotFound("id", id));
    }

    public void deleteByDeviceId(final UUID deviceId) {
        deviceDeviceConfigurationStore.deleteByDeviceId(deviceId);
    }

    public DeviceDeviceConfiguration save(final DeviceDeviceConfiguration deviceDeviceConfiguration) {
        return deviceDeviceConfigurationStore.save(deviceDeviceConfiguration);
    }

    public List<DeviceDeviceConfiguration> findByDeviceId(final UUID courseId) {
        return deviceDeviceConfigurationStore.findByDeviceId(courseId);
    }

    public DeviceDeviceConfiguration create(final DeviceDeviceConfiguration deviceDeviceConfiguration) {
        return deviceDeviceConfigurationStore.save(deviceDeviceConfiguration);
    }
}
