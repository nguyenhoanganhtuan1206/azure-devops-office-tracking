package com.openwt.officetracking.domain.office;

import com.openwt.officetracking.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class OfficeError {

    public static Supplier<NotFoundException> supplyOfficeNotFound(final String fieldName, final Object fieldValue) {
        return () -> new NotFoundException("Office with %s %s not found", fieldName, fieldValue);
    }
}