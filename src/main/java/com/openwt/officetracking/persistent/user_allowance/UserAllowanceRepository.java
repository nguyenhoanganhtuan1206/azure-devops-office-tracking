package com.openwt.officetracking.persistent.user_allowance;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserAllowanceRepository extends CrudRepository<UserAllowanceEntity, UUID> {
}
