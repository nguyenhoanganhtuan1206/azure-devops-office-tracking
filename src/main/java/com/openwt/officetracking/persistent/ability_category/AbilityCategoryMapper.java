package com.openwt.officetracking.persistent.ability_category;

import com.openwt.officetracking.domain.ability_category.AbilityCategory;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class AbilityCategoryMapper {

    public static AbilityCategory toAbilityCategory(final AbilityCategoryEntity abilityCategoryEntity) {
        return AbilityCategory.builder()
                .id(abilityCategoryEntity.getId())
                .name(abilityCategoryEntity.getName())
                .build();
    }

    public static List<AbilityCategory> toAbilityCategories(final List<AbilityCategoryEntity> abilityCategoryEntities) {
        return abilityCategoryEntities.stream()
                .map(AbilityCategoryMapper::toAbilityCategory)
                .toList();
    }
}
