package com.openwt.officetracking.fake;

import com.openwt.officetracking.api.device_configuration_value.DeviceConfigurationValueDTO;
import com.openwt.officetracking.api.device_configuration_value.DeviceConfigurationValueResponseDTO;
import com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValue;
import com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValueRequest;
import com.openwt.officetracking.persistent.device_configuration_value.DeviceConfigurationValueEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class DeviceConfigurationValueFakes {

    public static DeviceConfigurationValueEntity buildDeviceConfigurationValueEntity() {
        return DeviceConfigurationValueEntity.builder()
                .id(randomUUID())
                .value(randomAlphabetic(3, 10))
                .build();
    }

    public static List<DeviceConfigurationValueEntity> buildDeviceConfigurationValueEntities() {
        return buildList(DeviceConfigurationValueFakes::buildDeviceConfigurationValueEntity);
    }

    public static DeviceConfigurationValue buildDeviceConfigurationValue() {
        return DeviceConfigurationValue.builder()
                .id(randomUUID())
                .modelId(randomUUID())
                .configurationId(randomUUID())
                .value(randomAlphabetic(3, 10))
                .build();
    }

    public static DeviceConfigurationValueRequest buildDeviceConfigurationValueRequest() {
        return DeviceConfigurationValueRequest.builder()
                .id(randomUUID())
                .modelId(randomUUID())
                .configurationId(randomUUID())
                .value(randomAlphabetic(3, 10))
                .build();
    }

    public static DeviceConfigurationValueResponseDTO buildDeviceConfigurationValueRequestDTO() {
        return DeviceConfigurationValueResponseDTO.builder()
                .modelId(randomUUID())
                .configurationId(randomUUID())
                .value(randomAlphabetic(3, 10))
                .build();
    }

    public static List<DeviceConfigurationValue> buildDeviceConfigurationValues() {
        return buildList(DeviceConfigurationValueFakes::buildDeviceConfigurationValue);
    }

    public static DeviceConfigurationValueDTO buildDeviceConfigurationValueDTO() {
        return DeviceConfigurationValueDTO.builder()
                .id(randomUUID())
                .value(randomAlphabetic(3, 10))
                .build();
    }

    public static List<DeviceConfigurationValueDTO> buildDeviceConfigurationValueDTOs() {
        return buildList(DeviceConfigurationValueFakes::buildDeviceConfigurationValueDTO);
    }
}