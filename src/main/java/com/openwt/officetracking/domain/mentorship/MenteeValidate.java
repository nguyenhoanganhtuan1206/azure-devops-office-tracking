package com.openwt.officetracking.domain.mentorship;

import com.openwt.officetracking.error.BadRequestException;
import lombok.experimental.UtilityClass;

import java.util.UUID;
import java.util.function.Supplier;

import static com.openwt.officetracking.error.CommonError.supplyValidationError;

@UtilityClass
public class MenteeValidate {

    public static void validateUserId(final UUID userId) {
        if (userId == null) {
            throw supplyValidationError("Something went wrong assign this user as mentee. Please try again").get();
        }
    }

    public static Supplier<BadRequestException> supplyCannotInactive() {
        return () -> new BadRequestException("Inactivation is not allowed for objects with InProgress or Waiting course status");
    }

    public static Supplier<BadRequestException> supplyMenteeIsAlreadyActive() {
        return () -> new BadRequestException("This mentee is active already");
    }

    public static Supplier<BadRequestException> supplyMenteeIsAlreadyInactive() {
        return () -> new BadRequestException("This mentee is inactive already");
    }
}
