package com.openwt.officetracking.persistent.device_type;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceTypeRepository extends CrudRepository<DeviceTypeEntity, UUID> {

    Optional<DeviceTypeEntity> findByName(final String name);
}
