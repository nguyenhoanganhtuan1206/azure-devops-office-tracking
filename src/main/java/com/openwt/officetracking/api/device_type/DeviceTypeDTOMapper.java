package com.openwt.officetracking.api.device_type;

import com.openwt.officetracking.domain.device_type.DeviceType;
import com.openwt.officetracking.domain.device_type.DeviceTypeRequest;
import com.openwt.officetracking.domain.device_type.DeviceTypeResponse;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.api.device_configuration.DeviceConfigurationDTOMapper.toDeviceConfigurationRequestDTOs;
import static com.openwt.officetracking.api.device_model.DeviceModelDTOMapper.toDeviceModelDTOs;

@UtilityClass
public class DeviceTypeDTOMapper {

    public static DeviceTypeDTO toDeviceTypeDTO(final DeviceType deviceType) {
        return DeviceTypeDTO.builder()
                .id(deviceType.getId())
                .name(deviceType.getName())
                .build();
    }

    public static DeviceType toDeviceType(final DeviceTypeDTO deviceTypeDTO) {
        return DeviceType.builder()
                .id(deviceTypeDTO.getId())
                .name(deviceTypeDTO.getName())
                .build();
    }

    public static List<DeviceTypeDTO> toDeviceTypeDTOs(final List<DeviceType> deviceTypes) {
        return deviceTypes.stream()
                .map(DeviceTypeDTOMapper::toDeviceTypeDTO)
                .toList();
    }

    public static DeviceTypeResponseDTO toDeviceTypeResponseDTO(final DeviceTypeResponse deviceTypeResponse) {
        return DeviceTypeResponseDTO.builder()
                .id(deviceTypeResponse.getId())
                .name(deviceTypeResponse.getName())
                .models(toDeviceModelDTOs(deviceTypeResponse.getModels()))
                .build();
    }

    public static List<DeviceTypeResponseDTO> toDeviceTypeResponseDTOs(final List<DeviceTypeResponse> deviceTypeResponses) {
        return deviceTypeResponses.stream()
                .map(DeviceTypeDTOMapper::toDeviceTypeResponseDTO)
                .toList();
    }

    public static DeviceTypeRequest toDeviceTypeRequestDTO(final DeviceTypeRequestDTO deviceTypeRequestDTO) {
        return DeviceTypeRequest.builder()
                .modelId(deviceTypeRequestDTO.getModelId())
                .configurations(toDeviceConfigurationRequestDTOs(deviceTypeRequestDTO.getConfigurations()))
                .build();
    }
}