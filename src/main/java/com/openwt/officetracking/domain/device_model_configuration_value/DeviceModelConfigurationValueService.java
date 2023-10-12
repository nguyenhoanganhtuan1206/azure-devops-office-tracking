package com.openwt.officetracking.domain.device_model_configuration_value;

import com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValue;
import com.openwt.officetracking.persistent.device_model_configuration_value.DeviceModelConfigurationValueStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.domain.device_model_configuration_value.DeviceModelConfigurationValueError.supplyDeviceModelConfigurationValueNotFound;
import static com.openwt.officetracking.error.CommonError.supplyNotFoundError;


@Service
@RequiredArgsConstructor
public class DeviceModelConfigurationValueService {

    private final DeviceModelConfigurationValueStore deviceModelConfigurationValueStore;

    public DeviceModelConfigurationValue findById(final UUID id) {
        return deviceModelConfigurationValueStore.findById(id)
                .orElseThrow(supplyDeviceModelConfigurationValueNotFound("id", id));
    }

    public DeviceModelConfigurationValue findByValueId(final UUID valueId) {
        return deviceModelConfigurationValueStore.findByValueId(valueId)
                .orElseThrow(supplyDeviceModelConfigurationValueNotFound("valueId", valueId));
    }

    public List<DeviceModelConfigurationValue> findByModel(final UUID modelId) {
        return deviceModelConfigurationValueStore.findByDeviceModelId(modelId);
    }

    public List<DeviceModelConfigurationValue> findByModelAndConfiguration(final UUID modelId, final UUID configurationId) {
        return deviceModelConfigurationValueStore.findByDeviceModelAndDeviceConfiguration(modelId, configurationId);
    }

    public DeviceModelConfigurationValue findByModelAndConfigurationAndValue(final UUID modelId, final UUID configurationId, final UUID valueId) {
        return deviceModelConfigurationValueStore.findByDeviceModelAndDeviceConfigurationAndDeviceConfigurationValue(modelId, configurationId, valueId)
                .orElseThrow((supplyNotFoundError("This Device Model Configuration and Value is not existed!")));
    }

    public DeviceModelConfigurationValue save(final DeviceModelConfigurationValue deviceModelConfigurationValue) {
        return deviceModelConfigurationValueStore.save(deviceModelConfigurationValue);
    }

    public void deleteByDeviceModel(final UUID modelId) {
        deviceModelConfigurationValueStore.deleteByDeviceModel(modelId);
    }

    public void deleteByDeviceConfigurationValue(final DeviceConfigurationValue deviceConfigurationValue) {
        final DeviceModelConfigurationValue deviceModelConfigurationValue = findByModelAndConfigurationAndValue(deviceConfigurationValue.getModelId(), deviceConfigurationValue.getConfigurationId(), deviceConfigurationValue.getId());

        deviceModelConfigurationValue.setDeviceConfigurationValueId(null);
        deviceModelConfigurationValueStore.save(deviceModelConfigurationValue);
    }
}
