package com.openwt.officetracking.domain.ability_category;

import com.openwt.officetracking.persistent.ability_category.AbilityCategoryStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.openwt.officetracking.fake.AbilityCategoryFakes.buildAbilityCategories;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbilityCategoryServiceTest {

    @InjectMocks
    private AbilityCategoryService abilityCategoryService;

    @Mock
    private AbilityCategoryStore abilityCategoryStore;

    @Test
    void shouldFindAll_OK() {
        final var abilityCategories = buildAbilityCategories();

        when(abilityCategoryStore.findAll())
                .thenReturn(abilityCategories);

        final var actual = abilityCategoryService.findAll();

        assertEquals(abilityCategories, actual);

        verify(abilityCategoryStore).findAll();
    }
}
