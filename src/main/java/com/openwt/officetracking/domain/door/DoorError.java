package com.openwt.officetracking.domain.door;

import com.openwt.officetracking.error.BadRequestException;
import com.openwt.officetracking.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class DoorError {

    public static Supplier<BadRequestException> supplyDoorNameAlreadyExist(final Object fieldValue) {
        return () -> new BadRequestException("The door with name %s already exist", fieldValue);
    }

    public static Supplier<NotFoundException> supplyDoorNotFound(final String fieldName, final Object fieldValue) {
        return () -> new NotFoundException("Door with %s %s not found", fieldName, fieldValue);
    }
}
