package com.openwt.officetracking.domain.position;

import com.openwt.officetracking.error.BadRequestException;
import com.openwt.officetracking.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.UUID;
import java.util.function.Supplier;

@UtilityClass
public class PositionError {

    public static Supplier<NotFoundException> supplyPositionIdNotFound(final UUID id) {
        return () -> new NotFoundException("Position with id %s could not be found", id);
    }

    public static Supplier<BadRequestException> supplyPositionAlreadyExist(final String name) {
        return () -> new BadRequestException("Position with name %s already exist", name);
    }
}
