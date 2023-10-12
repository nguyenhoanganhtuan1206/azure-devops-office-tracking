package com.openwt.officetracking.domain.mentorship;

import com.openwt.officetracking.error.BadRequestException;
import lombok.experimental.UtilityClass;

import java.util.UUID;
import java.util.function.Supplier;

import static com.openwt.officetracking.error.CommonError.supplyValidationError;

@UtilityClass
public class MentorValidate {

    public static void validateUserId(final UUID userId) {
        if (userId == null) {
            throw supplyValidationError("Something went wrong assign this user as mentor. Please try again").get();
        }
    }

    public static Supplier<BadRequestException> supplyMentorIsAlreadyActive() {
        return () -> new BadRequestException("This mentor is active already");
    }

    public static Supplier<BadRequestException> supplyMentorIsAlreadyInactive() {
        return () -> new BadRequestException("This mentor is inactive already");
    }

    public static Supplier<BadRequestException> supplyCannotInactive() {
        return () -> new BadRequestException("This mentor has incomplete course/mentee");
    }
}
