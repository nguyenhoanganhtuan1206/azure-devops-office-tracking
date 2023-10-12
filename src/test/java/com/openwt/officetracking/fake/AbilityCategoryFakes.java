package com.openwt.officetracking.fake;

import com.openwt.officetracking.domain.ability_category.AbilityCategory;
import com.openwt.officetracking.persistent.ability_category.AbilityCategoryEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class AbilityCategoryFakes {

    public static AbilityCategoryEntity buildAbilityCategoryEntity() {
        return AbilityCategoryEntity.builder()
                .id(randomUUID())
                .name(randomAlphabetic(9))
                .build();
    }

    public static List<AbilityCategoryEntity> buildAbilityCategoryEntities() {
        return buildList(AbilityCategoryFakes::buildAbilityCategoryEntity);
    }

    public static AbilityCategory buildAbilityCategory() {
        return AbilityCategory.builder()
                .id(randomUUID())
                .name(randomAlphabetic(9))
                .build();
    }

    public static List<AbilityCategory> buildAbilityCategories() {
        return buildList(AbilityCategoryFakes::buildAbilityCategory);
    }
}
