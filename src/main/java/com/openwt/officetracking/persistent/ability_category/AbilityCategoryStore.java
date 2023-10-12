package com.openwt.officetracking.persistent.ability_category;

import com.openwt.officetracking.domain.ability_category.AbilityCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.openwt.officetracking.persistent.ability_category.AbilityCategoryMapper.toAbilityCategories;
import static org.apache.commons.collections4.IterableUtils.toList;

@Repository
@RequiredArgsConstructor
public class AbilityCategoryStore {

    private final AbilityCategoryRepository abilityCategoryRepository;

    public List<AbilityCategory> findAll() {
        return toAbilityCategories(toList(abilityCategoryRepository.findAll()));
    }
}
