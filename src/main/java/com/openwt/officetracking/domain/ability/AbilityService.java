package com.openwt.officetracking.domain.ability;

import com.openwt.officetracking.domain.ability_category.AbilityCategory;
import com.openwt.officetracking.domain.ability_category.AbilityCategoryService;
import com.openwt.officetracking.persistent.ability.AbilityStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.domain.ability.AbilityError.supplyAbilityNotFound;

@Service
@RequiredArgsConstructor
public class AbilityService {

    private final AbilityStore abilityStore;

    private final AbilityCategoryService abilityCategoryService;

    public Ability findById(final UUID abilityId) {
        return abilityStore.findById(abilityId)
                .orElseThrow(supplyAbilityNotFound("ability id", abilityId));
    }

    public List<Ability> findAll() {
        return abilityStore.findAll();
    }

    public List<Ability> findByAbilityCategoryId(final UUID abilityCategoryId) {
        return abilityStore.findByAbilityCategoryId(abilityCategoryId);
    }

    public List<AbilityCategoryDetail> findAbilityCategoryDetails() {
        return abilityCategoryService.findAll().stream()
                .map(this::buildAbilityCategoryDetail)
                .toList();
    }

    private AbilityCategoryDetail buildAbilityCategoryDetail(final AbilityCategory abilityCategory) {
        return AbilityCategoryDetail.builder()
                .abilities(findByAbilityCategoryId(abilityCategory.getId()))
                .id(abilityCategory.getId())
                .name(abilityCategory.getName())
                .build();
    }
}
