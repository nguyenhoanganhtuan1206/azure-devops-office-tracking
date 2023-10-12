package com.openwt.officetracking.api.device_configuration;

import com.openwt.officetracking.domain.device_configuration.DeviceConfiguration;
import com.openwt.officetracking.domain.device_configuration.DeviceConfigurationRequest;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.api.device_configuration_value.DeviceConfigurationValueDTOMapper.toDeviceConfigurationValueDTOs;
import static org.apache.commons.collections4.ListUtils.emptyIfNull;


@UtilityClass
public class DeviceConfigurationDTOMapper {

    public static DeviceConfigurationDTO toDeviceConfigurationDTO(final DeviceConfiguration deviceConfiguration) {
        return DeviceConfigurationDTO.builder()
                .id(deviceConfiguration.getId())
                .label(deviceConfiguration.getLabel())
                .values(toDeviceConfigurationValueDTOs(deviceConfiguration.getValues()))
                .build();
    }

    public static List<DeviceConfigurationDTO> toDeviceConfigurationDTOs(final List<DeviceConfiguration> deviceConfigurations) {
        return deviceConfigurations.stream()
                .map(DeviceConfigurationDTOMapper::toDeviceConfigurationDTO)
                .toList();
    }

    public static DeviceConfigurationRequest toDeviceConfigurationRequestDTO(final DeviceConfigurationRequestDTO deviceConfigurationRequestDTO) {
        return DeviceConfigurationRequest.builder()
                .id(deviceConfigurationRequestDTO.getId())
                .valueId(deviceConfigurationRequestDTO.getValueId())
                .build();
    }

    public static List<DeviceConfigurationRequest> toDeviceConfigurationRequestDTOs(final List<DeviceConfigurationRequestDTO> deviceConfigurationRequestDTOs) {
        return  emptyIfNull(deviceConfigurationRequestDTOs).stream()
                .map(DeviceConfigurationDTOMapper::toDeviceConfigurationRequestDTO)
                .toList();
    }
}