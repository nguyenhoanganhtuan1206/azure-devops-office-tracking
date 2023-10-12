package com.openwt.officetracking.persistent.device_configuration;

import com.openwt.officetracking.domain.device_configuration.DeviceConfiguration;
import lombok.experimental.UtilityClass;

import java.util.List;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@UtilityClass
public class DeviceConfigurationEntityMapper {

    public static DeviceConfiguration toDeviceConfiguration(final DeviceConfigurationEntity deviceConfigurationEntity) {
        return DeviceConfiguration.builder()
                .id(deviceConfigurationEntity.getId())
                .label(deviceConfigurationEntity.getLabel())
                .deviceTypeId(deviceConfigurationEntity.getDeviceTypeId())
                .build();
    }

    public static List<DeviceConfiguration> toDeviceConfigurations(final List<DeviceConfigurationEntity> deviceConfigurationEntities) {
        return emptyIfNull(deviceConfigurationEntities)
                .stream()
                .map(DeviceConfigurationEntityMapper::toDeviceConfiguration)
                .toList();
    }
}