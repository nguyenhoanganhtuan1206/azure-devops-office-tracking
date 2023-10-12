package com.openwt.officetracking.persistent.holiday;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HolidayRepository extends CrudRepository<HolidayEntity, UUID> {
}
