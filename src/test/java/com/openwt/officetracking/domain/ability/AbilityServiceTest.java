package com.openwt.officetracking.domain.ability;

import com.openwt.officetracking.domain.ability_category.AbilityCategoryService;
import com.openwt.officetracking.error.NotFoundException;
import com.openwt.officetracking.persistent.ability.AbilityStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.fake.AbilityCategoryFakes.buildAbilityCategories;
import static com.openwt.officetracking.fake.AbilityCategoryFakes.buildAbilityCategory;
import static com.openwt.officetracking.fake.AbilityFakes.buildAbilities;
import static com.openwt.officetracking.fake.AbilityFakes.buildAbility;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbilityServiceTest {

    @InjectMocks
    private AbilityService abilityService;

    @Mock
    private AbilityStore abilityStore;

    @Mock
    private AbilityCategoryService abilityCategoryService;

    @Test
    void shouldFindById_OK() {
        final var ability = buildAbility();

        when(abilityStore.findById(ability.getId()))
                .thenReturn(Optional.of(ability));

        final var actual = abilityService.findById(ability.getId());

        assertEquals(ability, actual);

        verify(abilityStore).findById(ability.getId());
    }

    @Test
    void shouldFindById_ThroughNotFoundException() {
        final var abilityId = randomUUID();

        when(abilityStore.findById(abilityId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> abilityService.findById(abilityId));

        verify(abilityStore).findById(abilityId);
    }

    @Test
    void shouldFindAll_OK() {
        final var abilities = buildAbilities();
        when(abilityStore.findAll())
                .thenReturn(abilities);

        final var actual = abilityService.findAll();

        assertEquals(abilities.size(), actual.size());
    }

    @Test
    void shouldFindByAbilityCategoryId_OK() {
        final var abilities = buildAbilities();
        final var categoryId = randomUUID();

        when(abilityStore.findByAbilityCategoryId(categoryId))
                .thenReturn(abilities);

        final var actual = abilityService.findByAbilityCategoryId(categoryId);

        assertEquals(abilities, actual);

        verify(abilityStore).findByAbilityCategoryId(categoryId);
    }

    @Test
    void shouldFindAbilityCatalog_OK() {
        final var abilityCategories = buildAbilityCategories();
        final var abilities = buildAbilities();
        final var abilityCategory = buildAbilityCategory();
        final var expected = AbilityCategoryDetail.builder()
                .id(abilityCategory.getId())
                .name(abilityCategory.getName())
                .abilities(abilities)
                .build();

        when(abilityCategoryService.findAll())
                .thenReturn(abilityCategories);
        when(abilityStore.findByAbilityCategoryId(any(UUID.class)))
                .thenReturn(abilities);

        final var result = abilityService.findAbilityCategoryDetails();

        assertEquals(expected.getAbilities().size(), result.get(0).getAbilities().size());

        verify(abilityCategoryService, times(1)).findAll();
        verify(abilityStore, times(4)).findByAbilityCategoryId(any(UUID.class));
    }
}
