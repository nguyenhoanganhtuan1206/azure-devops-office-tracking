package com.openwt.officetracking.domain.attendance;

import com.openwt.officetracking.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class TrackingHistoryError {

    public static Supplier<NotFoundException> supplyHistoryTrackingNotFound(final String fieldName, final Object fieldValue) {
        return () -> new NotFoundException("History with %s %s not found", fieldName, fieldValue);
    }
}
