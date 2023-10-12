package com.openwt.officetracking.domain.device_configuration_value;

import com.openwt.officetracking.domain.device_model_configuration_value.DeviceModelConfigurationValue;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DeviceConfigurationValueMapper {

    public static DeviceConfigurationValue toDeviceConfigurationValue(final DeviceConfigurationValueRequest deviceConfigurationValueRequest) {
        return DeviceConfigurationValue.builder()
                .modelId(deviceConfigurationValueRequest.getModelId())
                .configurationId(deviceConfigurationValueRequest.getConfigurationId())
                .value(deviceConfigurationValueRequest.getValue())
                .build();
    }

    public static DeviceModelConfigurationValue toDeviceModelConfigurationValue(final DeviceConfigurationValue deviceConfigurationValue) {
        return DeviceModelConfigurationValue.builder()
                .deviceModelId(deviceConfigurationValue.getModelId())
                .deviceConfigurationId(deviceConfigurationValue.getConfigurationId())
                .deviceConfigurationValueId(deviceConfigurationValue.getId())
                .build();
    }
}