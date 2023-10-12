package com.openwt.officetracking.api.device_model;

import com.openwt.officetracking.domain.device_model.DeviceModel;
import com.openwt.officetracking.domain.device_model.DeviceModelResponse;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.api.device_configuration.DeviceConfigurationDTOMapper.toDeviceConfigurationDTOs;

@UtilityClass
public class DeviceModelDTOMapper {

    public static DeviceModelDTO toDeviceModelDTO(final DeviceModelResponse deviceModelResponse) {
        return DeviceModelDTO.builder()
                .id(deviceModelResponse.getId())
                .name(deviceModelResponse.getName())
                .configurations(toDeviceConfigurationDTOs(deviceModelResponse.getConfigurations()))
                .build();
    }

    public static List<DeviceModelDTO> toDeviceModelDTOs(final List<DeviceModelResponse> deviceModelResponses) {
        return deviceModelResponses.stream()
                .map(DeviceModelDTOMapper::toDeviceModelDTO)
                .toList();
    }

    public static DeviceModel toDeviceModel(final DeviceModelRequestDTO deviceModelRequestDTO) {
        return DeviceModel.builder()
                .typeId(deviceModelRequestDTO.getTypeId())
                .typeId(deviceModelRequestDTO.getTypeId())
                .name(deviceModelRequestDTO.getName())
                .build();
    }

    public static DeviceModelResponseDTO toDeviceModelResponseDTO(final DeviceModel deviceModel) {
        return DeviceModelResponseDTO.builder()
                .id(deviceModel.getId())
                .typeId(deviceModel.getTypeId())
                .name(deviceModel.getName())
                .build();
    }

    public static List<DeviceModelResponseDTO> toDeviceModelResponseDTOs(final List<DeviceModel> deviceModels) {
        return deviceModels.stream()
                .map(DeviceModelDTOMapper::toDeviceModelResponseDTO)
                .toList();
    }
}
