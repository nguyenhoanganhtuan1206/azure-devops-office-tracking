package com.openwt.officetracking.persistent.door;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openwt.officetracking.fake.DoorFakes.buildDoorEntities;
import static com.openwt.officetracking.fake.DoorFakes.buildDoorEntity;
import static com.openwt.officetracking.persistent.door.DoorEntityMapper.toDoor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoorStoreTest {

    @Mock
    private DoorRepository doorRepository;

    @InjectMocks
    private DoorStore doorStore;

    @Test
    void shouldSave_OK() {
        final var beacon = buildDoorEntity();

        when(doorRepository.save(any(DoorEntity.class)))
                .thenReturn(beacon);

        final var actual = doorStore.save(toDoor(beacon));

        assertEquals(beacon.getId(), actual.getId());
        assertEquals(beacon.getName(), actual.getName());
        assertEquals(beacon.getMajor(), actual.getMajor());
        assertEquals(beacon.getMinor(), actual.getMinor());
        assertEquals(beacon.getOfficeId(), actual.getOfficeId());

        verify(doorRepository).save(any(DoorEntity.class));
    }

    @Test
    void shouldFindByDoorId_OK() {
        final var beacon = buildDoorEntity();

        when(doorRepository.findById(beacon.getId()))
                .thenReturn(Optional.of(beacon));

        final var actual = doorStore.findById(beacon.getId()).get();

        assertEquals(beacon.getId(), actual.getId());
        assertEquals(beacon.getName(), actual.getName());
        assertEquals(beacon.getMajor(), actual.getMajor());
        assertEquals(beacon.getMinor(), actual.getMinor());
        assertEquals(beacon.getOfficeId(), actual.getOfficeId());

        verify(doorRepository).findById(beacon.getId());
    }

    @Test
    void shouldFindByDoorName_OK() {
        final var beacon = buildDoorEntity();

        when(doorRepository.findByName(beacon.getName()))
                .thenReturn(Optional.of(beacon));

        final var actual = doorStore.findByName(beacon.getName()).get();

        assertEquals(beacon.getId(), actual.getId());
        assertEquals(beacon.getName(), actual.getName());
        assertEquals(beacon.getMajor(), actual.getMajor());
        assertEquals(beacon.getMinor(), actual.getMinor());
        assertEquals(beacon.getOfficeId(), actual.getOfficeId());

        verify(doorRepository).findByName(beacon.getName());
    }

    @Test
    void shouldFindByMajorAndMinor_OK() {
        final var beacon = buildDoorEntity();

        when(doorRepository.findByMajorAndMinor(beacon.getMajor(), beacon.getMinor()))
                .thenReturn(Optional.of(beacon));

        final var actual = doorStore.findByMajorAndMinor(beacon.getMajor(), beacon.getMinor()).get();

        assertEquals(beacon.getId(), actual.getId());
        assertEquals(beacon.getName(), actual.getName());
        assertEquals(beacon.getMajor(), actual.getMajor());
        assertEquals(beacon.getMinor(), actual.getMinor());
        assertEquals(beacon.getOfficeId(), actual.getOfficeId());

        verify(doorRepository).findByMajorAndMinor(beacon.getMajor(), beacon.getMinor());
    }

    @Test
    void findAll() {
        final var doorBeacons = buildDoorEntities();

        when(doorRepository.findAll())
                .thenReturn(doorBeacons);

        final var actual = doorStore.findAll();

        assertEquals(doorBeacons.size(), actual.size());

        verify(doorRepository).findAll();
    }

    @Test
    void shouldDelete_OK() {
        final var doorBeacon = buildDoorEntity();

        doNothing().when(doorRepository)
                .delete(any(DoorEntity.class));

        doorStore.delete(toDoor(doorBeacon));

        verify(doorRepository).delete(any(DoorEntity.class));
    }
}