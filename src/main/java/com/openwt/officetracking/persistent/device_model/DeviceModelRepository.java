package com.openwt.officetracking.persistent.device_model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceModelRepository extends CrudRepository<DeviceModelEntity, UUID> {

    List<DeviceModelEntity> findByTypeId(final UUID typeId);

    Optional<DeviceModelEntity> findByNameAndTypeId(final String name, final UUID typeId);
}
