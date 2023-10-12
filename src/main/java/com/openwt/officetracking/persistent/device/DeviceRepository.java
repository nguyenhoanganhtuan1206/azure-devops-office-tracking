package com.openwt.officetracking.persistent.device;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceRepository extends CrudRepository<DeviceEntity, UUID> {

    Optional<DeviceEntity> findBySerialNumber(final String serialNumber);

    @Query("SELECT d FROM DeviceEntity d WHERE d.isRequested = false")
    List<DeviceEntity> findAllDevices();

    @Query("SELECT d FROM DeviceEntity d WHERE d.requestUserId = :requestUserId AND d.isRequested = true")
    List<DeviceEntity> findAllRequests(final UUID requestUserId);

    @Query("SELECT d FROM DeviceEntity d WHERE d.isRequested = true")
    List<DeviceEntity> findAllRequests();

    @Query("SELECT d FROM DeviceEntity d " +
            " INNER JOIN DeviceAssignmentEntity da " +
            " ON d.id = da.deviceId " +
            " WHERE da.userId = :assignUserId ")
    List<DeviceEntity> findMyDevices(final UUID assignUserId);}
