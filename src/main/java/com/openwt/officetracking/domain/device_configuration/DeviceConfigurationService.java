package com.openwt.officetracking.domain.device_configuration;

import com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValue;
import com.openwt.officetracking.domain.device_configuration_value.DeviceConfigurationValueService;
import com.openwt.officetracking.domain.device_model_configuration_value.DeviceModelConfigurationValue;
import com.openwt.officetracking.domain.device_model_configuration_value.DeviceModelConfigurationValueService;
import com.openwt.officetracking.persistent.device_configuration.DeviceConfigurationStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.openwt.officetracking.domain.device_configuration.DeviceConfigurationError.supplyDeviceConfigurationNotFound;

@Service
@RequiredArgsConstructor
public class DeviceConfigurationService {

    private final DeviceConfigurationStore deviceConfigurationStore;

    private final DeviceConfigurationValueService deviceConfigurationValueService;

    private final DeviceModelConfigurationValueService deviceModelConfigurationValueService;

    public DeviceConfiguration findById(final UUID id) {
        return deviceConfigurationStore.findById(id)
                .orElseThrow(supplyDeviceConfigurationNotFound("id", id));
    }

    public List<DeviceConfiguration> findAllByDeviceTypeId(final UUID typeId) {
        return deviceConfigurationStore.findAllByTypeId(typeId);
    }

    public List<DeviceConfiguration> findAllByModelId(final UUID modelId) {
        final List<DeviceConfiguration> deviceConfigurations = new ArrayList<>();
        final List<DeviceModelConfigurationValue> deviceModelConfigurationValues = deviceModelConfigurationValueService.findByModel(modelId);

        final List<UUID> deviceConfigurationIds = deviceModelConfigurationValues.stream()
                .map(DeviceModelConfigurationValue::getDeviceConfigurationId)
                .distinct()
                .toList();

        deviceConfigurationIds.stream()
                .filter(Objects::nonNull)
                .map(this::findById)
                .map(configuration -> buildDeviceConfigurationValues(modelId, configuration))
                .forEach(deviceConfigurations::add);

        return deviceConfigurations;
    }

    private DeviceConfiguration buildDeviceConfigurationValues(final UUID modelId, final DeviceConfiguration configuration) {
        final List<DeviceConfigurationValue> configurationValues = deviceConfigurationValueService.findAllByModelAndDeviceConfiguration(modelId, configuration.getId());
        return configuration.withValues(configurationValues);
    }
}