package com.openwt.officetracking.persistent.door;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoorRepository extends CrudRepository<DoorEntity, UUID> {

    Optional<DoorEntity> findByName(final String name);

    Optional<DoorEntity> findByMajorAndMinor(final int major, final int minor);
}