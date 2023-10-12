package com.openwt.officetracking.domain.door;

import java.util.UUID;

import static com.openwt.officetracking.error.CommonError.supplyValidationError;
import static java.lang.String.format;
import static org.apache.logging.log4j.util.Strings.isBlank;

public class DoorValidation {

    private static final int MIN_MAJOR_MINOR_VALUE = 1;

    private static final int MAX_MAJOR_MINOR_VALUE = 65535;

    private static final int DOOR_NAME_MAX_LENGTH = 50;

    private static final int NOTE_MAX_LENGTH = 200;

    public static void validateDoor(final Door door) {
        validateDoorName(door.getName());
        validateMajor(door.getMajor());
        validateMinor(door.getMinor());
        validateOfficeId(door.getOfficeId());
        validateNote(door.getNote());
    }

    private static void validateDoorName(final String doorName) {
        if (isBlank(doorName)) {
            throw supplyValidationError("The door name cannot be blank").get();
        }

        if (doorName.length() > DOOR_NAME_MAX_LENGTH) {
            throw supplyValidationError(format("The door name cannot be over %d characters", DOOR_NAME_MAX_LENGTH)).get();
        }
    }

    private static void validateNote(final String note) {
        if (!isBlank(note) && note.length() > NOTE_MAX_LENGTH) {
            throw supplyValidationError(format("Note cannot be over %d characters", NOTE_MAX_LENGTH)).get();
        }
    }

    private static void validateMajor(final int major) {
        if (major < MIN_MAJOR_MINOR_VALUE || major > MAX_MAJOR_MINOR_VALUE) {
            throw supplyValidationError(format("The major value is invalid. The valid range is %d to %s.", MIN_MAJOR_MINOR_VALUE, MAX_MAJOR_MINOR_VALUE)).get();
        }
    }

    private static void validateMinor(final int minor) {
        if (minor < MIN_MAJOR_MINOR_VALUE || minor > MAX_MAJOR_MINOR_VALUE) {
            throw supplyValidationError(format("The minor value is invalid. The valid range is %d to %s.", MIN_MAJOR_MINOR_VALUE, MAX_MAJOR_MINOR_VALUE)).get();
        }
    }

    private static void validateOfficeId(final UUID officeRegionId) {
        if (officeRegionId == null) {
            throw supplyValidationError("Office Region cannot be empty.").get();
        }
    }
}