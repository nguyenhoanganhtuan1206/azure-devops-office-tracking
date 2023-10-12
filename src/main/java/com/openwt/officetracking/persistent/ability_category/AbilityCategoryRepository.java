package com.openwt.officetracking.persistent.ability_category;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AbilityCategoryRepository extends CrudRepository<AbilityCategoryEntity, UUID> {

}