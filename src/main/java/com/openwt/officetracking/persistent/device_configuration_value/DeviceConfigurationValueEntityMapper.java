package com.openwt.officetracking.persistent.device_configuration_value;

import com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValue;
import lombok.experimental.UtilityClass;

import java.util.List;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@UtilityClass
public class DeviceConfigurationValueEntityMapper {

    public static DeviceConfigurationValueEntity toDeviceConfigurationValueEntity(final DeviceConfigurationValue deviceConfigurationValue) {
        return DeviceConfigurationValueEntity.builder()
                .id(deviceConfigurationValue.getId())
                .value(deviceConfigurationValue.getValue())
                .build();
    }

    public static DeviceConfigurationValue toDeviceConfigurationValue(final DeviceConfigurationValueEntity deviceConfigurationValueEntity) {
        return DeviceConfigurationValue.builder()
                .id(deviceConfigurationValueEntity.getId())
                .value(deviceConfigurationValueEntity.getValue())
                .build();
    }

    public static List<DeviceConfigurationValue> toDeviceConfigurationValues(final List<DeviceConfigurationValueEntity> deviceConfigurationValueEntities) {
        return emptyIfNull(deviceConfigurationValueEntities)
                .stream()
                .map(DeviceConfigurationValueEntityMapper::toDeviceConfigurationValue)
                .toList();
    }
}