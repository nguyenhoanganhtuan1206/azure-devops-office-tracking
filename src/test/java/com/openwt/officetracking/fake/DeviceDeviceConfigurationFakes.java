package com.openwt.officetracking.fake;

import com.openwt.officetracking.domain.device_device_configuration.DeviceDeviceConfiguration;
import com.openwt.officetracking.persistent.device_device_configuration.DeviceDeviceConfigurationEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static java.util.UUID.randomUUID;

@UtilityClass
public class DeviceDeviceConfigurationFakes {

    public static DeviceDeviceConfigurationEntity buildDeviceDeviceConfigurationEntity() {
        return DeviceDeviceConfigurationEntity.builder()
                .id(randomUUID())
                .deviceId(randomUUID())
                .deviceModelConfigurationValueId(randomUUID())
                .build();
    }

    public static DeviceDeviceConfiguration buildDeviceDeviceConfiguration() {
        return DeviceDeviceConfiguration.builder()
                .id(randomUUID())
                .deviceId(randomUUID())
                .deviceModelConfigurationValueId(randomUUID())
                .build();
    }

    public static List<DeviceDeviceConfiguration> buildDeviceDeviceConfigurations() {
        return buildList(DeviceDeviceConfigurationFakes::buildDeviceDeviceConfiguration);
    }
}
