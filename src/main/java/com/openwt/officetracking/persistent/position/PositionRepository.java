package com.openwt.officetracking.persistent.position;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PositionRepository extends CrudRepository<PositionEntity, UUID> {

    Optional<PositionEntity> findByName(final String name);
}
