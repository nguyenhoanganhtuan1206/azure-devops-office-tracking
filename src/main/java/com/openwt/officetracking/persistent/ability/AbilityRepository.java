package com.openwt.officetracking.persistent.ability;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AbilityRepository extends CrudRepository<AbilityEntity, UUID> {

    List<AbilityEntity> findByAbilityCategoryId(final UUID abilityCategoryId);
}
