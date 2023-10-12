package com.openwt.officetracking.domain.device_model;

import com.openwt.officetracking.domain.device_configuration.DeviceConfiguration;
import com.openwt.officetracking.domain.device_configuration.DeviceConfigurationService;
import com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValue;
import com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValueService;
import com.openwt.officetracking.domain.device_device_configuration.DeviceDeviceConfiguration;
import com.openwt.officetracking.domain.device_model_configuration_value.DeviceModelConfigurationValue;
import com.openwt.officetracking.domain.device_model_configuration_value.DeviceModelConfigurationValueService;
import com.openwt.officetracking.persistent.device_device_configuration.DeviceDeviceConfigurationStore;
import com.openwt.officetracking.persistent.device_model.DeviceModelStore;
import com.openwt.officetracking.persistent.device_type.DeviceTypeStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.domain.device_model.DeviceModelError.*;
import static com.openwt.officetracking.domain.device_model.DeviceModelMapper.toDeviceModelResponses;
import static com.openwt.officetracking.domain.device_model.DeviceModelMapper.toModelConfigurationValue;
import static com.openwt.officetracking.domain.device_model.DeviceModelValidation.validateDeviceModel;

@Service
@RequiredArgsConstructor
public class DeviceModelService {

    private final DeviceModelStore deviceModelStore;

    private final DeviceTypeStore deviceTypeStore;

    private final DeviceConfigurationService deviceConfigurationService;

    private final DeviceConfigurationValueService deviceConfigurationValueService;

    private final DeviceDeviceConfigurationStore deviceDeviceConfigurationStore;

    private final DeviceModelConfigurationValueService deviceModelConfigurationValueService;

    public List<DeviceModelResponse> findAllByTypeId(final UUID typeId) {
        final List<DeviceModel> deviceModels = deviceModelStore.findByTypeId(typeId);
        final List<DeviceModelResponse> deviceModelResponses = toDeviceModelResponses(deviceModels);

        return deviceModelResponses.stream()
                .map(deviceModelResponse -> findByModelId(deviceModelResponse.getId()))
                .toList();
    }

    public DeviceModel findById(final UUID id) {
        return deviceModelStore.findById(id)
                .orElseThrow(supplyModelNotFound("id", id));
    }

    public List<DeviceModel> findByTypeId(final UUID typeId) {
        return deviceModelStore.findByTypeId(typeId);
    }

    public DeviceModelResponse findByModelId(final UUID modelId) {
        final DeviceModel deviceModel = findById(modelId);
        final List<DeviceConfiguration> deviceConfigurations = deviceConfigurationService.findAllByModelId(modelId);

        return DeviceModelResponse.builder()
                .id(modelId)
                .name(deviceModel.getName())
                .configurations(deviceConfigurations)
                .build();
    }

    @Transactional
    public DeviceModel create(final DeviceModel deviceModel) {
        validateDeviceModel(deviceModel);
        validateIfTypeNotFound(deviceModel.getTypeId());
        validateIfModelNameAvailable(deviceModel);

        final DeviceModel newDeviceModel = deviceModelStore.save(deviceModel);

        createNewModelConfigurationValue(newDeviceModel);

        return newDeviceModel;
    }

    @Transactional
    public DeviceModel update(final UUID modelId, final DeviceModel modelRequest) {
        validateDeviceModel(modelRequest);
        validateIfTypeNotFound(modelRequest.getTypeId());
        validateIfModelNameAvailable(modelRequest);

        final DeviceModel currentModel = findById(modelId);

        currentModel.setName(modelRequest.getName());
        currentModel.setTypeId(modelRequest.getTypeId());

        return deviceModelStore.save(currentModel);
    }

    @Transactional
    public void delete(final UUID modelId) {
        final DeviceModel currentModel = findById(modelId);

        if (handleModelConfigurationValueWhenDelete(currentModel)) {
            deviceModelConfigurationValueService.deleteByDeviceModel(modelId);
            deviceModelStore.delete(modelId);
        } else {
            throw supplyModelValidation("The device model cannot be deleted while it is still associated with other objects").get();
        }
    }

    private void createNewModelConfigurationValue(final DeviceModel deviceMode) {
        final List<DeviceConfiguration> deviceConfigurations = deviceConfigurationService.findAllByDeviceTypeId(deviceMode.getTypeId());

        if (deviceConfigurations.isEmpty()) {
            deviceModelConfigurationValueService.save(toModelConfigurationValue(deviceMode));
        }

        deviceConfigurations.forEach(deviceConfiguration -> {
            final DeviceModelConfigurationValue deviceModelConfigurationValue = toModelConfigurationValue(deviceMode)
                    .withDeviceConfigurationId(deviceConfiguration.getId());

            deviceModelConfigurationValueService.save(deviceModelConfigurationValue);
        });
    }

    private boolean handleModelConfigurationValueWhenDelete(final DeviceModel deviceModel) {
        final List<DeviceModelConfigurationValue> deviceModelConfigurationValues = deviceModelConfigurationValueService.findByModel(deviceModel.getId());

        return deviceModelConfigurationValues.stream().allMatch(deviceModelConfigurationValue -> {
            final DeviceConfigurationValue deviceConfigurationValue = deviceModelConfigurationValue.getDeviceConfigurationValueId() != null
                    ? deviceConfigurationValueService.findById(deviceModelConfigurationValue.getDeviceConfigurationValueId()) : null;

            final List<DeviceDeviceConfiguration> deviceDeviceConfigurationOptional = deviceDeviceConfigurationStore.findByModelConfigurationValueId(deviceModelConfigurationValue.getId());

            return deviceConfigurationValue == null && deviceDeviceConfigurationOptional.isEmpty();
        });
    }

    private void validateIfModelNameAvailable(final DeviceModel modelRequest) {
        deviceModelStore.findByNameAndTypeId(modelRequest)
                .ifPresent(existingModel -> {
                    throw supplyModelNameExisted("name", modelRequest.getName()).get();
                });
    }

    private void validateIfTypeNotFound(final UUID typeId) {
        if (deviceTypeStore.findById(typeId).isEmpty()) {
            throw supplyModelNotFound("typeId",typeId).get();
        }
    }
}
