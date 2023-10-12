package com.openwt.officetracking.domain.device_type;

import com.openwt.officetracking.error.BadRequestException;
import com.openwt.officetracking.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class DeviceTypeError {

    public static Supplier<NotFoundException> supplyDeviceTypeNotFound(final String fieldName, final Object fieldValue) {
        return () -> new NotFoundException("Device type with %s %s not found", fieldName, fieldValue);
    }

    public static Supplier<BadRequestException> supplyTypeExisted(final String fieldName, final Object fieldValue) {
        return () -> new BadRequestException("Device Type with %s %s already exists", fieldName, fieldValue);
    }
}