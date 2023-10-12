package com.openwt.officetracking.persistent.time_off_detail;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TimeOffDetailRepository extends CrudRepository<TimeOffDetailEntity, UUID> {
}
