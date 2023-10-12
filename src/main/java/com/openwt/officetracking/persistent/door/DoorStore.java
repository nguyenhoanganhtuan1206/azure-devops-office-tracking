package com.openwt.officetracking.persistent.door;

import com.openwt.officetracking.domain.door.Door;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.persistent.door.DoorEntityMapper.*;
import static org.apache.commons.collections4.IterableUtils.toList;

@Repository
@RequiredArgsConstructor
public class DoorStore {

    private final DoorRepository doorRepository;

    public List<Door> findAll() {
        return toDoors(toList(doorRepository.findAll()));
    }

    public Door save(final Door door) {
        return toDoor(doorRepository.save(toDoorEntity(door)));
    }

    public Optional<Door> findByName(final String name) {
        return doorRepository.findByName(name)
                .map(DoorEntityMapper::toDoor);
    }

    public Optional<Door> findByMajorAndMinor(final int major, final int minor) {
        return doorRepository.findByMajorAndMinor(major, minor)
                .map(DoorEntityMapper::toDoor);
    }

    public Optional<Door> findById(final UUID id) {
        return doorRepository.findById(id)
                .map(DoorEntityMapper::toDoor);
    }

    public void delete(final Door door) {
        doorRepository.delete(toDoorEntity(door));
    }
}