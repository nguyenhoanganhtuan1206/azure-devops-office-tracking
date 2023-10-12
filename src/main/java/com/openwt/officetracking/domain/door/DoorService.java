package com.openwt.officetracking.domain.door;

import com.openwt.officetracking.domain.office.OfficeService;
import com.openwt.officetracking.persistent.door.DoorStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.domain.door.DoorError.supplyDoorNameAlreadyExist;
import static com.openwt.officetracking.domain.door.DoorError.supplyDoorNotFound;
import static com.openwt.officetracking.domain.door.DoorValidation.validateDoor;
import static com.openwt.officetracking.error.CommonError.supplyValidationError;
import static java.time.Instant.now;

@Service
@RequiredArgsConstructor
public class DoorService {

    private final DoorStore doorStore;

    private final OfficeService officeService;

    public List<Door> findAll() {
        return doorStore.findAll();
    }

    public Door findByMajorAndMinor(final int major, final int minor) {
        return doorStore.findByMajorAndMinor(major, minor)
                .orElseThrow(supplyValidationError("This door already exists with both major and minor values."));
    }

    public Door save(final Door door) {
        validateDoor(door);
        verifyIfOfficeNotAvailable(door.getOfficeId());
        verifyIfDoorNameAvailable(door.getName());
        verifyIfMajorAndMinorAvailable(door.getMajor(), door.getMinor());

        return doorStore.save(door.withCreatedAt(now()));
    }

    public Door findById(final UUID id) {
        return doorStore.findById(id)
                .orElseThrow(supplyDoorNotFound("id", id));
    }

    public Door update(final UUID id, final Door doorUpdate) {
        final Door currentDoor = findById(id);
        validateDoor(doorUpdate);
        verifyIfOfficeNotAvailable(doorUpdate.getOfficeId());

        if (!currentDoor.getName().equals(doorUpdate.getName())) {
            verifyIfDoorNameAvailable(doorUpdate.getName());

            currentDoor.setName(doorUpdate.getName());
        }

        if (currentDoor.getMajor() != doorUpdate.getMajor() || currentDoor.getMinor() != doorUpdate.getMinor()) {
            verifyIfMajorAndMinorAvailable(doorUpdate.getMajor(), doorUpdate.getMinor());

            currentDoor.setMajor(doorUpdate.getMajor());
            currentDoor.setMinor(doorUpdate.getMinor());
        }

        return doorStore.save(updateCurrentDoorProperties(currentDoor, doorUpdate));
    }

    public void delete(final UUID id) {
        doorStore.delete(findById(id));
    }

    private Door updateCurrentDoorProperties(final Door currentDoor, final Door doorUpdate) {
        return currentDoor
                .withNote(doorUpdate.getNote())
                .withMinor(doorUpdate.getMinor())
                .withMajor(doorUpdate.getMajor())
                .withOfficeId(doorUpdate.getOfficeId());
    }

    private void verifyIfDoorNameAvailable(final String doorName) {
        doorStore.findByName(doorName).ifPresent(beacon -> {
            throw supplyDoorNameAlreadyExist(beacon.getName()).get();
        });
    }

    private void verifyIfMajorAndMinorAvailable(final int major, final int minor) {
        doorStore.findByMajorAndMinor(major, minor).ifPresent(_beacon -> {
            throw supplyValidationError("This door already exists with both major and minor values.").get();
        });
    }

    private void verifyIfOfficeNotAvailable(final UUID officeId) {
        officeService.findById(officeId);
    }
}
