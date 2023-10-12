package com.openwt.officetracking.persistent.device_assignment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceAssignmentRepository extends CrudRepository<DeviceAssignmentEntity, UUID> {

    Optional<DeviceAssignmentEntity> findByUserIdAndDeviceId(final UUID userId, final UUID deviceId);

    List<DeviceAssignmentEntity> findByDeviceId(final UUID deviceId);
}
