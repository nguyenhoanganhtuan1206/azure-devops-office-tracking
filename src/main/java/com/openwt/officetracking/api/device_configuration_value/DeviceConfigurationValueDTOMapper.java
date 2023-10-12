package com.openwt.officetracking.api.device_configuration_value;

import com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValue;
import com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValueRequest;
import lombok.experimental.UtilityClass;

import java.util.List;


@UtilityClass
public class DeviceConfigurationValueDTOMapper {

    public static DeviceConfigurationValueDTO toDeviceConfigurationValueDTO(final DeviceConfigurationValue deviceConfigurationValue) {
        return DeviceConfigurationValueDTO.builder()
                .id(deviceConfigurationValue.getId())
                .value(deviceConfigurationValue.getValue())
                .build();
    }

    public static List<DeviceConfigurationValueDTO> toDeviceConfigurationValueDTOs(final List<DeviceConfigurationValue> deviceConfigurationValues) {
        return deviceConfigurationValues.stream()
                .map(DeviceConfigurationValueDTOMapper::toDeviceConfigurationValueDTO)
                .toList();
    }

    public static DeviceConfigurationValueRequest toDeviceConfigurationValueRequest(final DeviceConfigurationValueRequestDTO deviceConfigurationValueRequestDTO) {
        return DeviceConfigurationValueRequest.builder()
                .modelId(deviceConfigurationValueRequestDTO.getModelId())
                .configurationId(deviceConfigurationValueRequestDTO.getConfigurationId())
                .value(deviceConfigurationValueRequestDTO.getValue())
                .build();
    }

    public static DeviceConfigurationValueResponseDTO toDeviceConfigurationValueResponseDTO(final DeviceConfigurationValue deviceConfigurationValue) {
        return DeviceConfigurationValueResponseDTO.builder()
                .id(deviceConfigurationValue.getId())
                .modelId(deviceConfigurationValue.getModelId())
                .configurationId(deviceConfigurationValue.getConfigurationId())
                .value(deviceConfigurationValue.getValue())
                .build();
    }

    public static List<DeviceConfigurationValueResponseDTO> toDeviceConfigurationValueResponseDTOs(final List<DeviceConfigurationValue> deviceConfigurationValues) {
        return deviceConfigurationValues.stream()
                .map(DeviceConfigurationValueDTOMapper::toDeviceConfigurationValueResponseDTO)
                .toList();
    }
}