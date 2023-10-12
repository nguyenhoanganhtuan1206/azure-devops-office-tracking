package com.openwt.officetracking.domain.device_model;

import com.openwt.officetracking.error.BadRequestException;
import com.openwt.officetracking.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class DeviceModelError {

    public static Supplier<NotFoundException> supplyModelNotFound(final String fieldName, final Object fieldValue) {
        return () -> new NotFoundException("Model with %s %s not found", fieldName, fieldValue);
    }

    public static Supplier<BadRequestException> supplyModelNameExisted(final String fieldName, final Object fieldValue) {
        return () -> new BadRequestException("Model with %s %s already exists", fieldName, fieldValue);
    }

    public static Supplier<BadRequestException> supplyModelValidation(final String message) {
        return () -> new BadRequestException(message);
    }
}
