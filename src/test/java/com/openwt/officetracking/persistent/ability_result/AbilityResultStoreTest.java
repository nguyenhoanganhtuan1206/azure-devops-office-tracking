package com.openwt.officetracking.persistent.ability_result;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.AbilityResultFakes.buildAbilityResultEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbilityResultStoreTest {

    @InjectMocks
    private AbilityResultStore abilityResultStore;

    @Mock
    private AbilityResultRepository abilityResultRepository;

    @Test
    void shouldFindByAbilityIdAndCourseAssignmentId_OK() {
        final var abilityResult = buildAbilityResultEntity();

        when(abilityResultRepository.findByAbilityIdAndCourseAssignmentIdAndMentor(abilityResult.getAbilityId(), abilityResult.getCourseAssignmentId()))
                .thenReturn(Optional.of(abilityResult));

        final var actual = abilityResultStore.findByAbilityIdAndCourseAssignmentIdAndMentor(abilityResult.getAbilityId(), abilityResult.getCourseAssignmentId()).get();

        assertEquals(abilityResult.getAbilityId(), actual.getAbilityId());
        assertEquals(abilityResult.getPoint(), actual.getPoint());
        assertEquals(abilityResult.getCourseAssignmentId(), actual.getCourseAssignmentId());
        assertEquals(abilityResult.getCreatedAt(), actual.getCreatedAt());

        verify(abilityResultRepository).findByAbilityIdAndCourseAssignmentIdAndMentor(abilityResult.getAbilityId(), abilityResult.getCourseAssignmentId());
    }

}
