package com.openwt.officetracking.persistent.office;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OfficeRepository extends CrudRepository<OfficeEntity, UUID> {
}