package com.openwt.officetracking.persistent.ability_category;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.openwt.officetracking.fake.AbilityCategoryFakes.buildAbilityCategoryEntities;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbilityCategoryStoreTest {

    @InjectMocks
    private AbilityCategoryStore abilityCategoryStore;

    @Mock
    private AbilityCategoryRepository abilityCategoryRepository;

    @Test
    void shouldFindAll_OK() {
        final var abilityCategories = buildAbilityCategoryEntities();

        when(abilityCategoryRepository.findAll())
                .thenReturn(abilityCategories);

        final var actual = abilityCategoryStore.findAll();

        assertEquals(abilityCategories.size(), actual.size());

        verify(abilityCategoryRepository).findAll();
    }
}
