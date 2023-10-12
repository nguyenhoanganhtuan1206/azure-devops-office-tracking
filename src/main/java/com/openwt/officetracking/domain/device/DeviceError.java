package com.openwt.officetracking.domain.device;

import com.openwt.officetracking.error.BadRequestException;
import com.openwt.officetracking.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class DeviceError {

    public static Supplier<NotFoundException> supplyDeviceNotFound(final String fieldName, final Object fieldValue) {
        return () -> new NotFoundException("Device with %s %s not found", fieldName, fieldValue);
    }

    public static Supplier<BadRequestException> supplyDeviceExisted(final String fieldName, final Object fieldValue) {
        return () -> new BadRequestException("Device with %s %s has been taken", fieldName, fieldValue);
    }

    public static Supplier<BadRequestException> supplyDeviceValidation(final String message) {
        return () -> new BadRequestException(message);
    }
}
