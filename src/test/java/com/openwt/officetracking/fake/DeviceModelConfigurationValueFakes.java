package com.openwt.officetracking.fake;

import com.openwt.officetracking.domain.device_model_configuration_value.DeviceModelConfigurationValue;
import com.openwt.officetracking.persistent.device_model_configuration_value.DeviceModelConfigurationValueEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static java.util.UUID.randomUUID;

@UtilityClass
public class DeviceModelConfigurationValueFakes {

    public static DeviceModelConfigurationValueEntity buildDeviceModelConfigurationValueEntity() {
        return DeviceModelConfigurationValueEntity.builder()
                .id(randomUUID())
                .deviceModelId(randomUUID())
                .deviceConfigurationId(randomUUID())
                .deviceConfigurationValueId(randomUUID())
                .build();
    }

    public static List<DeviceModelConfigurationValueEntity> buildDeviceModelConfigurationValueEntities() {
        return buildList(DeviceModelConfigurationValueFakes::buildDeviceModelConfigurationValueEntity);
    }

    public static DeviceModelConfigurationValue buildDeviceModelConfigurationValue() {
        return DeviceModelConfigurationValue.builder()
                .id(randomUUID())
                .deviceModelId(randomUUID())
                .deviceConfigurationId(randomUUID())
                .deviceConfigurationValueId(randomUUID())
                .build();
    }

    public static List<DeviceModelConfigurationValue> buildDeviceModelConfigurationValues() {
        return buildList(DeviceModelConfigurationValueFakes::buildDeviceModelConfigurationValue);
    }
}
