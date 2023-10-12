package com.openwt.officetracking.persistent.device_model_configuration_value;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceModelConfigurationValueRepository extends CrudRepository<DeviceModelConfigurationValueEntity, UUID> {

    List<DeviceModelConfigurationValueEntity> findByDeviceModelIdAndDeviceConfigurationId(final UUID modelId, final UUID configurationId);

    Optional<DeviceModelConfigurationValueEntity> findByDeviceModelIdAndDeviceConfigurationIdAndDeviceConfigurationValueId(final UUID modelId, final UUID configurationId, final UUID valueId);

    List<DeviceModelConfigurationValueEntity> findByDeviceModelId(final UUID modelId);

    Optional<DeviceModelConfigurationValueEntity> findByDeviceConfigurationValueId(final UUID valueId);

    void deleteByDeviceModelId(final UUID deviceModelId);
}
