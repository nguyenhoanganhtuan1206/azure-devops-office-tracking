package com.openwt.officetracking.persistent.time_off_type;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TimeOffTypeRepository extends CrudRepository<TimeOffTypeEntity, UUID> {
}
