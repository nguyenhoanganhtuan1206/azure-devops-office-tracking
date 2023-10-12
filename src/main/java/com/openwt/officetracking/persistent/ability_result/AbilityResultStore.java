package com.openwt.officetracking.persistent.ability_result;

import com.openwt.officetracking.domain.ability_result.AbilityResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.persistent.ability_result.AbilityResultEntityMapper.*;

@Repository
@RequiredArgsConstructor
public class AbilityResultStore {

    private final AbilityResultRepository abilityResultRepository;

    public Optional<AbilityResult> findByAbilityIdAndCourseAssignmentIdAndMentor(final UUID abilityId, final UUID courseAssignmentId) {
        return abilityResultRepository.findByAbilityIdAndCourseAssignmentIdAndMentor(abilityId, courseAssignmentId)
                .map(AbilityResultEntityMapper::toAbilityResult);
    }

    public Optional<AbilityResult> findByAbilityIdAndCourseAssignmentIdAndNotMentor(final UUID abilityId, final UUID courseAssignmentId) {
        return abilityResultRepository.findByAbilityIdAndCourseAssignmentIdAndNotMentor(abilityId, courseAssignmentId)
                .map(AbilityResultEntityMapper::toAbilityResult);
    }

    public List<AbilityResult> findByAbilityIdAndNotMentor(final UUID abilityId) {
        return toAbilityResults(abilityResultRepository.findByAbilityIdAndNotMentor(abilityId));
    }

    public AbilityResult save(final AbilityResult abilityResult) {
        return toAbilityResult(abilityResultRepository.save(toAbilityResultEntity(abilityResult)));
    }
}
