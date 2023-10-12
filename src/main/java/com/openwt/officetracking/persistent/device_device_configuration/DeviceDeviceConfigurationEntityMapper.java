package com.openwt.officetracking.persistent.device_device_configuration;

import com.openwt.officetracking.domain.device_device_configuration.DeviceDeviceConfiguration;
import lombok.experimental.UtilityClass;

import java.util.List;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@UtilityClass
public class DeviceDeviceConfigurationEntityMapper {

    public static DeviceDeviceConfiguration toDeviceDeviceConfiguration(final DeviceDeviceConfigurationEntity deviceDeviceConfigurationEntity) {
        return DeviceDeviceConfiguration.builder()
                .id(deviceDeviceConfigurationEntity.getId())
                .deviceId(deviceDeviceConfigurationEntity.getDeviceId())
                .deviceModelConfigurationValueId(deviceDeviceConfigurationEntity.getDeviceModelConfigurationValueId())
                .build();
    }

    public static List<DeviceDeviceConfiguration> toDeviceDeviceConfigurations(final List<DeviceDeviceConfigurationEntity> deviceDeviceConfigurationEntities) {
        return emptyIfNull(deviceDeviceConfigurationEntities)
                .stream()
                .map(DeviceDeviceConfigurationEntityMapper::toDeviceDeviceConfiguration)
                .toList();
    }

    public static DeviceDeviceConfigurationEntity toDeviceDeviceConfigurationEntity(final DeviceDeviceConfiguration deviceDeviceConfiguration) {
        return DeviceDeviceConfigurationEntity.builder()
                .id(deviceDeviceConfiguration.getId())
                .deviceId(deviceDeviceConfiguration.getDeviceId())
                .deviceModelConfigurationValueId(deviceDeviceConfiguration.getDeviceModelConfigurationValueId())
                .build();
    }
}
