package com.openwt.officetracking.fake;

import com.openwt.officetracking.api.device_configuration.DeviceConfigurationRequestDTO;
import com.openwt.officetracking.domain.device_configuration.DeviceConfiguration;
import com.openwt.officetracking.domain.device_configuration.DeviceConfigurationRequest;
import com.openwt.officetracking.persistent.device_configuration.DeviceConfigurationEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static com.openwt.officetracking.fake.DeviceConfigurationValueFakes.buildDeviceConfigurationValues;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class DeviceConfigurationFakes {

    public static DeviceConfigurationEntity buildDeviceConfigurationEntity() {
        return DeviceConfigurationEntity.builder()
                .id(randomUUID())
                .label(randomAlphabetic(3, 10))
                .build();
    }

    public static List<DeviceConfigurationEntity> buildDeviceConfigurationEntities() {
        return buildList(DeviceConfigurationFakes::buildDeviceConfigurationEntity);
    }

    public static DeviceConfiguration buildDeviceConfiguration() {
        return DeviceConfiguration.builder()
                .id(randomUUID())
                .label(randomAlphabetic(3, 10))
                .values(buildDeviceConfigurationValues())
                .build();
    }

    public static List<DeviceConfiguration> buildDeviceConfigurations() {
        return buildList(DeviceConfigurationFakes::buildDeviceConfiguration);
    }

    public static DeviceConfigurationRequestDTO buildDeviceConfigurationRequestDTO() {
        return DeviceConfigurationRequestDTO.builder()
                .id(randomUUID())
                .valueId(randomUUID())
                .build();
    }

    public static List<DeviceConfigurationRequestDTO> buildDeviceConfigurationRequestDTOs() {
        return buildList(DeviceConfigurationFakes::buildDeviceConfigurationRequestDTO);
    }

    public static DeviceConfigurationRequest buildDeviceConfigurationRequest() {
        return DeviceConfigurationRequest.builder()
                .id(randomUUID())
                .valueId(randomUUID())
                .build();
    }

    public static List<DeviceConfigurationRequest> buildDeviceConfigurationRequests() {
        return buildList(DeviceConfigurationFakes::buildDeviceConfigurationRequest);
    }
}
