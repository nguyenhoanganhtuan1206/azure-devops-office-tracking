package com.openwt.officetracking.domain.device_model;

import com.openwt.officetracking.domain.device_model_configuration_value.DeviceModelConfigurationValue;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class DeviceModelMapper {

    public static DeviceModelResponse toDeviceModelResponse(final DeviceModel deviceModel) {
        return DeviceModelResponse.builder()
                .id(deviceModel.getId())
                .name(deviceModel.getName())
                .build();
    }

    public static List<DeviceModelResponse> toDeviceModelResponses(final List<DeviceModel> deviceModels) {
        return deviceModels.stream()
                .map(DeviceModelMapper::toDeviceModelResponse)
                .toList();
    }

    public static DeviceModelConfigurationValue toModelConfigurationValue(final DeviceModel deviceModel) {
        return DeviceModelConfigurationValue.builder()
                .deviceModelId(deviceModel.getId())
                .deviceConfigurationId(null)
                .deviceConfigurationValueId(null)
                .build();
    }
}