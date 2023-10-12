package com.openwt.officetracking.persistent.time_off_request;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TimeOffRequestRepository extends CrudRepository<TimeOffRequestEntity, UUID> {
}
