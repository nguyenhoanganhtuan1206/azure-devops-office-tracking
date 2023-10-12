package com.openwt.officetracking.persistent.device_type;

import com.openwt.officetracking.domain.device_type.DeviceType;
import lombok.experimental.UtilityClass;

import java.util.List;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@UtilityClass
public class DeviceTypeEntityMapper {

    public static DeviceType toDeviceType(final DeviceTypeEntity deviceTypeEntity) {
        return DeviceType.builder()
                .id(deviceTypeEntity.getId())
                .name(deviceTypeEntity.getName())
                .build();
    }

    public static DeviceTypeEntity toDeviceTypeEntity(final DeviceType deviceType) {
        return DeviceTypeEntity.builder()
                .id(deviceType.getId())
                .name(deviceType.getName())
                .build();
    }

    public static List<DeviceType> toDeviceTypes(final List<DeviceTypeEntity> deviceTypeEntities) {
        return emptyIfNull(deviceTypeEntities)
                .stream()
                .map(DeviceTypeEntityMapper::toDeviceType)
                .toList();
    }
}
