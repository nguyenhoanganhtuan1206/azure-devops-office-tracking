package com.openwt.officetracking.persistent.device_configuration_value;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeviceConfigurationValueRepository extends CrudRepository<DeviceConfigurationValueEntity, UUID> {
}
