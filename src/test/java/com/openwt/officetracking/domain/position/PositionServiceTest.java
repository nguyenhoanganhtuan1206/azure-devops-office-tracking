package com.openwt.officetracking.domain.position;

import com.openwt.officetracking.error.BadRequestException;
import com.openwt.officetracking.error.NotFoundException;
import com.openwt.officetracking.persistent.position.PositionStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.PositionFakes.buildPosition;
import static com.openwt.officetracking.fake.PositionFakes.buildPositions;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PositionServiceTest {

    @Mock
    private PositionStore positionStore;
    @InjectMocks
    private PositionService positionService;

    @Test
    void shouldFindAll_Ok() {
        final var expected = buildPositions();

        when(positionStore.findAll()).thenReturn(expected);

        final var actual = positionService.findAll();

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getId(), actual.get(0).getId());
        assertEquals(expected.get(0).getName(), actual.get(0).getName());
        assertEquals(expected.get(0).isTemporary(), actual.get(0).isTemporary());

        verify(positionStore).findAll();
    }

    @Test
    void shouldFindById_Ok() {
        final var position = buildPosition();

        when(positionStore.findById(position.getId())).thenReturn(Optional.of(position));

        assertEquals(position, positionService.findById(position.getId()));

        verify(positionStore).findById(position.getId());
    }

    @Test
    void shouldFindById_ThrownNotFound() {
        final var uuid = randomUUID();

        when(positionStore.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> positionService.findById(uuid));

        verify(positionStore).findById(uuid);
    }

    @Test
    void shouldCreate_OK() {
        final var position = buildPosition();

        when(positionStore.save(argThat(p -> p.equals(position)))).thenReturn(position);

        final var actual = positionService.create(position);

        assertEquals(position, actual);

        verify(positionStore).save(argThat(p -> p.equals(position)));
    }

    @Test
    void shouldCreate_ThrownBadRequest() {
        final var position = buildPosition()
                .withName(null);

        assertThrows(BadRequestException.class, () -> positionService.create(position));

        verify(positionStore, never()).save(position);
    }

    @Test
    void shouldCreate_ThrownPositionAvailable() {
        final var position = buildPosition();

        when(positionStore.findByName(position.getName())).thenReturn(Optional.of(position));

        assertThrows(BadRequestException.class, () -> positionService.create(position));

        verify(positionStore).findByName(position.getName());
    }

    @Test
    void shouldUpdate_OK() {
        final var position = buildPosition();
        final var positionUpdate = buildPosition()
                .withId(position.getId());

        when(positionStore.findById(position.getId())).thenReturn(Optional.of(position));
        when(positionStore.save(position)).thenReturn(position);

        final var actual = positionService.update(position.getId(), positionUpdate);

        assertEquals(positionUpdate.getId(), actual.getId());
        assertEquals(positionUpdate.getName(), actual.getName());
        assertEquals(positionUpdate.isTemporary(), actual.isTemporary());

        verify(positionStore).findById(position.getId());
        verify(positionStore).save(position);
    }

    @Test
    void shouldUpdate_ThrownNotFound() {
        final var positionId = randomUUID();
        final var positionUpdate = buildPosition();

        when(positionStore.findById(positionId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> positionService.update(positionId, positionUpdate));

        verify(positionStore).findById(positionId);
    }

    @Test
    void shouldUpdate_ThrownPositionAvailable() {
        final var positionToUpdate = buildPosition();
        final var positionExited = buildPosition();
        final var positionUpdate = buildPosition()
                .withName(positionExited.getName());

        when(positionStore.findById(positionToUpdate.getId())).thenReturn(Optional.of(positionToUpdate));
        when(positionStore.findByName(positionUpdate.getName())).thenReturn(Optional.of(positionUpdate));

        assertThrows(BadRequestException.class, () -> positionService.update(positionToUpdate.getId(), positionUpdate));

        verify(positionStore).findById(positionToUpdate.getId());
        verify(positionStore).findByName(positionUpdate.getName());
        verify(positionStore, never()).save(positionUpdate);
    }

    @Test
    void shouldDelete_OK() {
        final var position = buildPosition();

        when(positionStore.findById(position.getId())).thenReturn(Optional.of(position));

        positionService.delete(position.getId());

        verify(positionStore).findById(position.getId());
    }

    @Test
    void shouldDeleteId_ThrownNotFound() {
        final var uuid = randomUUID();

        when(positionStore.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> positionService.delete(uuid));

        verify(positionStore).findById(uuid);
    }
}