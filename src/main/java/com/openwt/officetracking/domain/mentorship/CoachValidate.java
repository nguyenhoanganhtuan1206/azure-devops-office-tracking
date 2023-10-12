package com.openwt.officetracking.domain.mentorship;

import com.openwt.officetracking.error.BadRequestException;
import lombok.experimental.UtilityClass;

import java.util.UUID;
import java.util.function.Supplier;

import static com.openwt.officetracking.error.CommonError.supplyValidationError;

@UtilityClass
public class CoachValidate {

    public static void validateUserId(final UUID userId) {
        if (userId == null) {
            throw supplyValidationError("Something went wrong assign this user as coach. Please try again").get();
        }
    }

    public static Supplier<BadRequestException> supplyCoachIsAlreadyActive() {
        return () -> new BadRequestException("This coach is active already");
    }

    public static Supplier<BadRequestException> supplyCoachIsAlreadyInactive() {
        return () -> new BadRequestException("This coach is inactive already");
    }
}
