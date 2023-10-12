package com.openwt.officetracking.persistent.ability;

import com.openwt.officetracking.domain.ability.Ability;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class AbilityEntityMapper {

    public static Ability toAbility(final AbilityEntity abilityEntity) {
        return Ability.builder()
                .id(abilityEntity.getId())
                .name(abilityEntity.getName())
                .abilityCategoryId(abilityEntity.getAbilityCategoryId())
                .build();
    }

    public static List<Ability> toAbilities(final List<AbilityEntity> abilityEntities) {
        return abilityEntities.stream()
                .map(AbilityEntityMapper::toAbility)
                .toList();
    }

    public static AbilityEntity toAbilityEntity(final Ability ability) {
        return AbilityEntity.builder()
                .id(ability.getId())
                .name(ability.getName())
                .abilityCategoryId(ability.getAbilityCategoryId())
                .build();
    }

    public static List<AbilityEntity> toAbilityEntities(final List<Ability> abilities) {
        return abilities.stream()
                .map(AbilityEntityMapper::toAbilityEntity)
                .toList();
    }
}
