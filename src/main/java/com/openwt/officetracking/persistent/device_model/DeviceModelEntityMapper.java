package com.openwt.officetracking.persistent.device_model;

import com.openwt.officetracking.domain.device_model.DeviceModel;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class DeviceModelEntityMapper {

    public static DeviceModel toDeviceModel(final DeviceModelEntity deviceModelEntity) {
        return DeviceModel.builder()
                .id(deviceModelEntity.getId())
                .typeId(deviceModelEntity.getTypeId())
                .name(deviceModelEntity.getName())
                .build();
    }

    public static List<DeviceModel> toDeviceModels(final List<DeviceModelEntity> deviceModelEntities) {
        return deviceModelEntities.stream()
                .map(DeviceModelEntityMapper::toDeviceModel)
                .toList();
    }

    public static DeviceModelEntity toDeviceModelEntity(final DeviceModel deviceModel) {
        return DeviceModelEntity.builder()
                .id(deviceModel.getId())
                .typeId(deviceModel.getTypeId())
                .name(deviceModel.getName())
                .build();
    }
}
