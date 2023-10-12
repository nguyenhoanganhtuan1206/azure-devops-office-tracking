package com.openwt.officetracking.fake;

import com.openwt.officetracking.api.device_type.DeviceTypeRequestDTO;
import com.openwt.officetracking.domain.device_type.DeviceType;
import com.openwt.officetracking.domain.device_type.DeviceTypeRequest;
import com.openwt.officetracking.domain.device_type.DeviceTypeResponse;
import com.openwt.officetracking.persistent.device_type.DeviceTypeEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static com.openwt.officetracking.fake.DeviceConfigurationFakes.buildDeviceConfigurationRequestDTOs;
import static com.openwt.officetracking.fake.DeviceConfigurationFakes.buildDeviceConfigurationRequests;
import static com.openwt.officetracking.fake.DeviceModelFakes.buildDeviceModelResponses;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class DeviceTypeFakes {

    public static DeviceTypeEntity buildDeviceTypeEntity() {
        return DeviceTypeEntity.builder()
                .id(randomUUID())
                .name(randomAlphabetic(3, 10))
                .build();
    }

    public static DeviceType buildDeviceType() {
        return DeviceType.builder()
                .id(randomUUID())
                .name(randomAlphabetic(3, 10))
                .build();
    }

    public static DeviceTypeResponse buildDeviceTypeResponse() {
        return DeviceTypeResponse.builder()
                .id(randomUUID())
                .name(randomAlphabetic(3, 10))
                .models(buildDeviceModelResponses())
                .build();
    }

    public static List<DeviceTypeResponse> buildDeviceTypeResponses() {
        return buildList(DeviceTypeFakes::buildDeviceTypeResponse);
    }

    public static DeviceTypeRequestDTO buildDeviceTypeRequestDTO() {
        return DeviceTypeRequestDTO.builder()
                .modelId(randomUUID())
                .configurations(buildDeviceConfigurationRequestDTOs())
                .build();
    }

    public static DeviceTypeRequest buildDeviceTypeRequest() {
        return DeviceTypeRequest.builder()
                .modelId(randomUUID())
                .configurations(buildDeviceConfigurationRequests())
                .build();
    }
}
