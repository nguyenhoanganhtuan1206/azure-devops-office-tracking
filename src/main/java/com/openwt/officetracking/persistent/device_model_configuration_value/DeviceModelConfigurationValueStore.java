package com.openwt.officetracking.persistent.device_model_configuration_value;

import com.openwt.officetracking.domain.device_model_configuration_value.DeviceModelConfigurationValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.persistent.device_model_configuration_value.DeviceModelConfigurationValueEntityMapper.*;

@Repository
@RequiredArgsConstructor
public class DeviceModelConfigurationValueStore {

    private final DeviceModelConfigurationValueRepository deviceModelConfigurationValueRepository;

    public Optional<DeviceModelConfigurationValue> findById(final UUID id) {
        return deviceModelConfigurationValueRepository.findById(id)
                .map(DeviceModelConfigurationValueEntityMapper::toDeviceModelConfigurationValue);
    }

    public Optional<DeviceModelConfigurationValue> findByValueId(final UUID id) {
        return deviceModelConfigurationValueRepository.findByDeviceConfigurationValueId(id)
                .map(DeviceModelConfigurationValueEntityMapper::toDeviceModelConfigurationValue);
    }

    public List<DeviceModelConfigurationValue> findByDeviceModelId(final UUID modelId) {
        return toDeviceModelConfigurationsValues(deviceModelConfigurationValueRepository.findByDeviceModelId(modelId));
    }

    public List<DeviceModelConfigurationValue> findByDeviceModelAndDeviceConfiguration(final UUID modelId, final UUID configurationId) {
        return toDeviceModelConfigurationsValues(deviceModelConfigurationValueRepository.findByDeviceModelIdAndDeviceConfigurationId(modelId, configurationId));
    }

    public Optional<DeviceModelConfigurationValue> findByDeviceModelAndDeviceConfigurationAndDeviceConfigurationValue(final UUID modelId, final UUID configurationId, final UUID valueId) {
        return deviceModelConfigurationValueRepository.findByDeviceModelIdAndDeviceConfigurationIdAndDeviceConfigurationValueId(modelId, configurationId, valueId)
                .map(DeviceModelConfigurationValueEntityMapper::toDeviceModelConfigurationValue);
    }

    public DeviceModelConfigurationValue save(final DeviceModelConfigurationValue deviceModelConfigurationValue) {
        return toDeviceModelConfigurationValue(deviceModelConfigurationValueRepository.save(toDeviceModelConfigurationValueEntity(deviceModelConfigurationValue)));
    }

    public void deleteByDeviceModel(final UUID modelId) {
        deviceModelConfigurationValueRepository.deleteByDeviceModelId(modelId);
    }

    public void deleteById(final UUID id) {
        deviceModelConfigurationValueRepository.deleteById(id);
    }
}
