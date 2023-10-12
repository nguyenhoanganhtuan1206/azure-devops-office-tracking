package com.openwt.officetracking.domain.ability_category;

import com.openwt.officetracking.persistent.ability_category.AbilityCategoryStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AbilityCategoryService {

    private final AbilityCategoryStore abilityCategoryStore;

    public List<AbilityCategory> findAll() {
        return abilityCategoryStore.findAll();
    }
}
