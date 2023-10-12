package com.openwt.officetracking.persistent.position;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.PositionFakes.buildPositionEntities;
import static com.openwt.officetracking.fake.PositionFakes.buildPositionEntity;
import static com.openwt.officetracking.persistent.position.PositionEntityMapper.toPosition;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PositionStoreTest {

    @Mock
    private PositionRepository positionRepository;

    @InjectMocks
    private PositionStore positionStore;

    @Test
    void shouldFindAll_OK() {
        final var expected = buildPositionEntities();

        when(positionRepository.findAll()).thenReturn(expected);

        final var actual = positionStore.findAll();

        assertEquals(expected.size(), actual.size());

        verify(positionRepository).findAll();
    }

    @Test
    void shouldFindById_OK() {
        final var position = buildPositionEntity();
        final var positionOptional = Optional.of(position);

        when(positionRepository.findById(position.getId())).thenReturn(positionOptional);

        final var actual = positionStore.findById(position.getId()).get();
        final var expected = positionOptional.get();

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.isTemporary(), actual.isTemporary());

        verify(positionRepository).findById(position.getId());
    }

    @Test
    void shouldFindById_Empty() {
        final var uuid = randomUUID();

        when(positionRepository.findById(uuid)).thenReturn(Optional.empty());

        assertFalse(positionStore.findById(uuid).isPresent());
        verify(positionRepository).findById(uuid);
    }

    @Test
    void shouldFindByName_Ok() {
        final var position = buildPositionEntity();
        final var positionOptional = Optional.of(position);

        when(positionRepository.findByName(position.getName())).thenReturn(positionOptional);

        assertEquals(positionOptional, positionRepository.findByName(position.getName()));

        verify(positionRepository).findByName(position.getName());
    }

    @Test
    void shouldFindByName_Empty() {
        final var search = randomAlphabetic(6, 10);

        when(positionRepository.findByName(search)).thenReturn(Optional.empty());

        assertFalse(positionStore.findByName(search).isPresent());

        verify(positionRepository).findByName(search);
    }

    @Test
    void shouldSave_OK() {
        final var positionSave = buildPositionEntity();

        final ArgumentMatcher<PositionEntity> positionEntityMatcher = entity -> entity.getId().equals(positionSave.getId())
                && entity.getName().equals(positionSave.getName())
                && entity.isTemporary() == positionSave.isTemporary();

        when(positionRepository.save(argThat(positionEntityMatcher))).thenReturn(positionSave);

        final var actual = positionStore.save(toPosition(positionSave));

        assertEquals(positionSave.getId().toString(), actual.getId().toString());
        assertEquals(positionSave.getName(), actual.getName());
        assertEquals(positionSave.isTemporary(), actual.isTemporary());

        verify(positionRepository).save(argThat(positionEntityMatcher));
    }

    @Test
    void shouldDelete_OK() {
        final var id = randomUUID();

        doNothing().when(positionRepository).deleteById(id);

        positionStore.delete(id);

        verify(positionRepository).deleteById(id);
    }
}