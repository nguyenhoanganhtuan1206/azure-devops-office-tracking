package com.openwt.officetracking.domain.office;

import lombok.experimental.UtilityClass;

import static com.openwt.officetracking.error.CommonError.supplyValidationError;
import static java.lang.String.format;
import static java.util.UUID.fromString;
import static org.apache.commons.lang3.StringUtils.isBlank;

@UtilityClass
public class OfficeValidation {

    private static final int OFFICE_NAME_MAX_LENGTH = 100;

    private static final int MIN_OFFICE_RADIUS = 0;

    private static final int MAX_OFFICE_RADIUS = 500;

    public static void validateOfficeInformation(final Office office) {
        validateOfficeName(office.getName());
        validateOfficeId(office.getOfficeUUID());
        validateOfficeLongitude(office.getLongitude());
        validateOfficeLatitude(office.getLatitude());
        validateOfficeRadius(office.getRadius());
    }

    private static void validateOfficeName(final String officeName) {
        if (isBlank(officeName)) {
            throw supplyValidationError("Office name cannot be blank").get();
        }

        if (officeName.length() > OFFICE_NAME_MAX_LENGTH) {
            throw supplyValidationError("Office name cannot be over 100 characters").get();
        }
    }

    private static void validateOfficeId(final String officeUUID) {
        if (isBlank(officeUUID)) {
            throw supplyValidationError("Office id cannot be blank").get();
        }

        if (!isValidUUID(officeUUID)) {
            throw supplyValidationError("Office UUID must be UUID format").get();
        }
    }

    private static void validateOfficeLongitude(final Double longitude) {
        if (longitude == null) {
            throw supplyValidationError("Office longitude cannot be blank").get();
        }
    }

    private static void validateOfficeLatitude(final Double latitude) {
        if (latitude == null) {
            throw supplyValidationError("Office latitude cannot be blank").get();
        }
    }

    private static void validateOfficeRadius(final Double radius) {
        if (radius == null) {
            throw supplyValidationError("Office radius cannot be blank").get();
        }

        if (radius < MIN_OFFICE_RADIUS || radius > MAX_OFFICE_RADIUS) {
            throw supplyValidationError(format("Office radius must be between %d and %s", MIN_OFFICE_RADIUS, MAX_OFFICE_RADIUS)).get();
        }
    }

    private static boolean isValidUUID(final String uuidStr) {
        try {
            fromString(uuidStr);
            return true;
        } catch (final IllegalArgumentException ex) {
            return false;
        }
    }
}
