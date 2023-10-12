package com.openwt.officetracking.fake;

import com.openwt.officetracking.domain.ability.Ability;
import com.openwt.officetracking.persistent.ability.AbilityEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
@UtilityClass
public class AbilityFakes {
    public static AbilityEntity buildAbilityEntity() {
        return AbilityEntity.builder()
                .id(randomUUID())
                .name(randomAlphabetic(9))
                .abilityCategoryId(randomUUID())
                .build();
    }
    public static List<AbilityEntity> buildAbilityEntities() {
        return buildList(AbilityFakes::buildAbilityEntity);
    }

    public static Ability buildAbility() {
        return Ability.builder()
                .id(randomUUID())
                .name(randomAlphabetic(9))
                .abilityCategoryId(randomUUID())
                .build();
    }
    public static List<Ability> buildAbilities() {
        return buildList(AbilityFakes::buildAbility);
    }
}