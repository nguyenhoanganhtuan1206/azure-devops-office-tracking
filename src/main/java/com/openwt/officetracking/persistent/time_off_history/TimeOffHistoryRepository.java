package com.openwt.officetracking.persistent.time_off_history;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TimeOffHistoryRepository extends CrudRepository<TimeOffHistoryEntity, UUID> {
}
