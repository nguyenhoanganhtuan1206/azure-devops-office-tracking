package com.openwt.officetracking.persistent.ability;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.openwt.officetracking.fake.AbilityFakes.buildAbilityEntities;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AbilityStoreTest {

    @Mock
    private AbilityRepository abilityRepository;

    @InjectMocks
    private AbilityStore abilityStore;

    @Test
    void shouldFindAll_OK() {
        final var abilities = buildAbilityEntities();

        when(abilityRepository.findAll())
                .thenReturn(abilities);

        final var actual = abilityStore.findAll();

        assertEquals(abilities.size(), actual.size());

        verify(abilityRepository).findAll();
    }
}
