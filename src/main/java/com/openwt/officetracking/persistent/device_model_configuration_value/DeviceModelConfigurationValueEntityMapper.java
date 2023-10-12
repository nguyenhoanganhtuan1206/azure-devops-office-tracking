package com.openwt.officetracking.persistent.device_model_configuration_value;

import com.openwt.officetracking.domain.device_model_configuration_value.DeviceModelConfigurationValue;
import lombok.experimental.UtilityClass;

import java.util.List;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@UtilityClass
public class DeviceModelConfigurationValueEntityMapper {

    public static DeviceModelConfigurationValue toDeviceModelConfigurationValue(final DeviceModelConfigurationValueEntity deviceModelConfigurationValueEntity) {
        return DeviceModelConfigurationValue.builder()
                .id(deviceModelConfigurationValueEntity.getId())
                .deviceModelId(deviceModelConfigurationValueEntity.getDeviceModelId())
                .deviceConfigurationId(deviceModelConfigurationValueEntity.getDeviceConfigurationId())
                .deviceConfigurationValueId(deviceModelConfigurationValueEntity.getDeviceConfigurationValueId())
                .build();
    }

    public static List<DeviceModelConfigurationValue> toDeviceModelConfigurationsValues(final List<DeviceModelConfigurationValueEntity> deviceModelConfigurationValueEntities) {
        return emptyIfNull(deviceModelConfigurationValueEntities)
                .stream()
                .map(DeviceModelConfigurationValueEntityMapper::toDeviceModelConfigurationValue)
                .toList();
    }

    public static DeviceModelConfigurationValueEntity toDeviceModelConfigurationValueEntity(final DeviceModelConfigurationValue deviceModelConfigurationValue) {
        return DeviceModelConfigurationValueEntity.builder()
                .id(deviceModelConfigurationValue.getId())
                .deviceModelId(deviceModelConfigurationValue.getDeviceModelId())
                .deviceConfigurationId(deviceModelConfigurationValue.getDeviceConfigurationId())
                .deviceConfigurationValueId(deviceModelConfigurationValue.getDeviceConfigurationValueId())
                .build();
    }
}
