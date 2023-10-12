package com.openwt.officetracking.domain.mentorship;

import com.openwt.officetracking.error.BadRequestException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class MentorshipError {

    public static Supplier<BadRequestException> supplyPermissionUserAsAdmin() {
        return () -> new BadRequestException("Users with the Admin role are not allowed to assign");
    }
}
