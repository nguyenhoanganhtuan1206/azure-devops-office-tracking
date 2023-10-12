package com.openwt.officetracking.persistent.time_off_reason;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TimeOffReasonRepository extends CrudRepository<TimeOffReasonEntity, UUID> {
}
