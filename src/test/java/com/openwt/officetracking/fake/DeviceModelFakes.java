package com.openwt.officetracking.fake;

import com.openwt.officetracking.api.device_model.DeviceModelRequestDTO;
import com.openwt.officetracking.domain.device_model.DeviceModel;
import com.openwt.officetracking.domain.device_model.DeviceModelResponse;
import com.openwt.officetracking.persistent.device_model.DeviceModelEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static com.openwt.officetracking.fake.DeviceConfigurationFakes.buildDeviceConfigurations;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class DeviceModelFakes {

    public static DeviceModelResponse buildDeviceModelResponse() {
        return DeviceModelResponse.builder()
                .id(randomUUID())
                .name(randomAlphabetic(3, 10))
                .configurations(buildDeviceConfigurations())
                .build();
    }

    public static List<DeviceModelResponse> buildDeviceModelResponses() {
        return buildList(DeviceModelFakes::buildDeviceModelResponse);
    }

    public static DeviceModelEntity buildDeviceModelEntity() {
        return DeviceModelEntity.builder()
                .id(randomUUID())
                .typeId(randomUUID())
                .name(randomAlphabetic(3, 10))
                .build();
    }

    public static DeviceModel buildDeviceModel() {
        return DeviceModel.builder()
                .id(randomUUID())
                .name(randomAlphabetic(3, 10))
                .typeId(randomUUID())
                .build();
    }

    public static List<DeviceModel> buildDeviceModels() {
        return buildList(DeviceModelFakes::buildDeviceModel);
    }

    public static List<DeviceModelEntity> buildDeviceModelEntities() {
        return buildList(DeviceModelFakes::buildDeviceModelEntity);
    }

    public static DeviceModelRequestDTO buildDeviceModelRequestDTO() {
        return DeviceModelRequestDTO.builder()
                .typeId(randomUUID())
                .name(randomAlphabetic(3, 10))
                .build();
    }
}
