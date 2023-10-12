package com.openwt.officetracking.persistent.ability_result;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AbilityResultRepository extends CrudRepository<AbilityResultEntity, UUID> {

    Optional<AbilityResultEntity> findByCourseAssignmentId(final UUID courseAssignmentId);

    @Query("SELECT a FROM AbilityResultEntity a WHERE a.abilityId = :abilityId AND a.courseAssignmentId = :courseAssignmentId AND a.isMentor = true")
    Optional<AbilityResultEntity> findByAbilityIdAndCourseAssignmentIdAndMentor(final UUID abilityId, final UUID courseAssignmentId);

    @Query("SELECT a FROM AbilityResultEntity a WHERE a.abilityId = :abilityId AND a.courseAssignmentId = :courseAssignmentId AND a.isMentor = false")
    Optional<AbilityResultEntity> findByAbilityIdAndCourseAssignmentIdAndNotMentor(final UUID abilityId, final UUID courseAssignmentId);


    @Query("SELECT a FROM AbilityResultEntity a WHERE a.courseAssignmentId = :courseAssignmentId AND a.isMentor = false")
    Optional<AbilityResultEntity> findByCourseAssignmentIdAndNotMentor(final UUID courseAssignmentId);

    @Query("SELECT a FROM AbilityResultEntity a WHERE a.abilityId = :abilityId AND a.isMentor = false")
    List<AbilityResultEntity> findByAbilityIdAndNotMentor(final UUID abilityId);
}
