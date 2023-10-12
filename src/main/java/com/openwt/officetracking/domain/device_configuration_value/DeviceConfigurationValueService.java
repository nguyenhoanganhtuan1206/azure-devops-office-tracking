package com.openwt.officetracking.domain.device_configuration_value;

import com.openwt.officetracking.domain.device_device_configuration.DeviceDeviceConfiguration;
import com.openwt.officetracking.domain.device_model_configuration_value.DeviceModelConfigurationValue;
import com.openwt.officetracking.domain.device_model_configuration_value.DeviceModelConfigurationValueService;
import com.openwt.officetracking.persistent.device_configuration.DeviceConfigurationStore;
import com.openwt.officetracking.persistent.device_configuration_value.DeviceConfigurationValueStore;
import com.openwt.officetracking.persistent.device_device_configuration.DeviceDeviceConfigurationStore;
import com.openwt.officetracking.persistent.device_model.DeviceModelStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValueError.supplyDeviceConfigurationValueNotFound;
import static com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValueError.supplyValueNameExisted;
import static com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValueMapper.toDeviceConfigurationValue;
import static com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValueMapper.toDeviceModelConfigurationValue;
import static com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValueValidation.validateDeviceConfigurationRequest;
import static com.openwt.officetracking.domain.device_model.DeviceModelError.supplyModelNotFound;
import static com.openwt.officetracking.domain.device_model.DeviceModelError.supplyModelValidation;

@Service
@RequiredArgsConstructor
public class DeviceConfigurationValueService {

    private final DeviceConfigurationValueStore deviceConfigurationValueStore;

    private final DeviceModelConfigurationValueService deviceModelConfigurationValueService;

    private final DeviceModelStore deviceModelStore;

    private final DeviceConfigurationStore deviceConfigurationStore;

    private final DeviceDeviceConfigurationStore deviceDeviceConfigurationStore;

    public DeviceConfigurationValue findById(final UUID id) {
        return deviceConfigurationValueStore.findById(id)
                .orElseThrow(supplyDeviceConfigurationValueNotFound("id", id));
    }

    public List<DeviceConfigurationValue> findAllByModelAndDeviceConfiguration(final UUID modelId, final UUID configurationId) {
        final List<DeviceModelConfigurationValue> deviceModelConfigurationValues = deviceModelConfigurationValueService.findByModelAndConfiguration(modelId, configurationId);

        final List<UUID> deviceConfigurationValueIds = deviceModelConfigurationValues.stream()
                .map(DeviceModelConfigurationValue::getDeviceConfigurationValueId)
                .toList();

        return deviceConfigurationValueIds.stream()
                .filter(Objects::nonNull)
                .map(this::findById)
                .toList();
    }

    public DeviceConfigurationValue findDetailById(final UUID id) {
        final DeviceConfigurationValue deviceConfigurationValue = findById(id);

        final DeviceModelConfigurationValue deviceModelConfigurationValue = deviceModelConfigurationValueService.findByValueId(deviceConfigurationValue.getId());

        return deviceConfigurationValue
                .withModelId(deviceModelConfigurationValue.getDeviceModelId())
                .withConfigurationId(deviceModelConfigurationValue.getDeviceConfigurationId());
    }

    @Transactional
    public DeviceConfigurationValue create(final DeviceConfigurationValueRequest deviceConfigurationValueRequest) {
        validateDeviceConfigurationRequest(deviceConfigurationValueRequest);
        validateIfModelNotFound(deviceConfigurationValueRequest.getModelId());
        validateIfConfigurationNotFound(deviceConfigurationValueRequest.getConfigurationId());
        validateIfValueNameAvailable(deviceConfigurationValueRequest);

        final DeviceConfigurationValue newDeviceConfigurationValue = deviceConfigurationValueStore.save(toDeviceConfigurationValue(deviceConfigurationValueRequest));
        final DeviceConfigurationValue detailDeviceConfigurationValue = updateProperties(newDeviceConfigurationValue, deviceConfigurationValueRequest);

        updateModelConfigurationValue(detailDeviceConfigurationValue);

        return detailDeviceConfigurationValue;
    }

    @Transactional
    public DeviceConfigurationValue update(final UUID valueId, final DeviceConfigurationValueRequest deviceConfigurationValueRequest) {
        validateDeviceConfigurationRequest(deviceConfigurationValueRequest);
        validateIfModelNotFound(deviceConfigurationValueRequest.getModelId());
        validateIfConfigurationNotFound(deviceConfigurationValueRequest.getConfigurationId());
        validateIfValueNameAvailable(deviceConfigurationValueRequest);

        final DeviceConfigurationValue deviceConfigurationValue = findDetailById(valueId);

        deviceConfigurationValue.setValue(deviceConfigurationValueRequest.getValue());

        deviceConfigurationValueStore.save(deviceConfigurationValue);

        return  deviceConfigurationValue;
    }

    @Transactional
    public void delete(final UUID valueId) {
        final DeviceConfigurationValue deviceConfigurationValue = findDetailById(valueId);

        if (handleModelConfigurationValueWhenDelete(deviceConfigurationValue)) {
            deviceModelConfigurationValueService.deleteByDeviceConfigurationValue(deviceConfigurationValue);
            deviceConfigurationValueStore.delete(valueId);
        } else {
            throw supplyModelValidation("Value cannot be deleted while it is still associated with other device").get();
        }
    }

    private void updateModelConfigurationValue(final DeviceConfigurationValue deviceConfigurationValue) {
        final List<DeviceModelConfigurationValue> deviceModelConfigurationValues = deviceModelConfigurationValueService.findByModelAndConfiguration(deviceConfigurationValue.getModelId(), deviceConfigurationValue.getConfigurationId());

        boolean nullValueFound = false;
        for (final DeviceModelConfigurationValue modelConfigValue : deviceModelConfigurationValues) {
            if (modelConfigValue.getDeviceConfigurationValueId() == null) {
                modelConfigValue.setDeviceConfigurationValueId(deviceConfigurationValue.getId());
                deviceModelConfigurationValueService.save(modelConfigValue);
                nullValueFound = true;
                break;
            }
        }

        if (!nullValueFound) {
            final DeviceModelConfigurationValue newModelConfigValue = toDeviceModelConfigurationValue(deviceConfigurationValue);
            deviceModelConfigurationValueService.save(newModelConfigValue);
        }
    }

    private DeviceConfigurationValue updateProperties(final DeviceConfigurationValue deviceConfigurationValue, final DeviceConfigurationValueRequest deviceConfigurationValueRequest) {
        return deviceConfigurationValue
                .withModelId(deviceConfigurationValueRequest.getModelId())
                .withConfigurationId(deviceConfigurationValueRequest.getConfigurationId());
    }

    private boolean handleModelConfigurationValueWhenDelete(final DeviceConfigurationValue deviceConfigurationValue) {
        final DeviceModelConfigurationValue deviceModelConfigurationValue = deviceModelConfigurationValueService.findByModelAndConfigurationAndValue(deviceConfigurationValue.getModelId(), deviceConfigurationValue.getConfigurationId(), deviceConfigurationValue.getId());
        final List<DeviceDeviceConfiguration> deviceDeviceConfigurationOptional = deviceDeviceConfigurationStore.findByModelConfigurationValueId(deviceModelConfigurationValue.getId());

        return deviceDeviceConfigurationOptional.isEmpty();
    }

    private void validateIfModelNotFound(final UUID modelId) {
        if (deviceModelStore.findById(modelId).isEmpty()) {
            throw supplyModelNotFound("modelId", modelId).get();
        }
    }

    private void validateIfConfigurationNotFound(final UUID configurationId) {
        if (deviceConfigurationStore.findById(configurationId).isEmpty()) {
            throw supplyModelNotFound("configurationId", configurationId).get();
        }
    }

    private void validateIfValueNameAvailable(final DeviceConfigurationValueRequest deviceConfigurationValueRequest) {
        final List<DeviceModelConfigurationValue> deviceModelConfigurationValues = deviceModelConfigurationValueService.findByModelAndConfiguration(deviceConfigurationValueRequest.getModelId(), deviceConfigurationValueRequest.getConfigurationId());

        for (final DeviceModelConfigurationValue modelConfigValue : deviceModelConfigurationValues) {
            if (modelConfigValue.getDeviceConfigurationValueId() != null) {
                final DeviceConfigurationValue deviceConfigurationValue = findById(modelConfigValue.getDeviceConfigurationValueId());

                if (deviceConfigurationValue.getValue().equals(deviceConfigurationValueRequest.getValue())) {
                    throw supplyValueNameExisted("name", deviceConfigurationValueRequest.getValue()).get();
                }
            }
        }
    }
}