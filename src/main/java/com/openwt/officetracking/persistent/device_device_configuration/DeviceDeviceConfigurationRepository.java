package com.openwt.officetracking.persistent.device_device_configuration;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeviceDeviceConfigurationRepository extends CrudRepository<DeviceDeviceConfigurationEntity, UUID> {

    void deleteByDeviceId(final UUID deviceId);

    List<DeviceDeviceConfigurationEntity> findByDeviceId(final UUID deviceId);

    List<DeviceDeviceConfigurationEntity> findByDeviceModelConfigurationValueId(final UUID deviceModelConfigurationValueId);
}
