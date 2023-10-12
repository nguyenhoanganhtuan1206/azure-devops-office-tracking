package com.openwt.officetracking.persistent.ability;
import com.openwt.officetracking.domain.ability.Ability;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.persistent.ability.AbilityEntityMapper.toAbilities;
import static org.apache.commons.collections4.IterableUtils.toList;
@Repository
@RequiredArgsConstructor
public class AbilityStore {

    private final AbilityRepository abilityRepository;

    public Optional<Ability> findById(final UUID abilityId) {
        return abilityRepository.findById(abilityId)
                .map(AbilityEntityMapper::toAbility);
    }

    public List<Ability> findAll() {
        return toAbilities(toList(abilityRepository.findAll()));
    }

    public List<Ability> findByAbilityCategoryId(final UUID abilityCategoryId) {
        return toAbilities(abilityRepository.findByAbilityCategoryId(abilityCategoryId));
    }
}

