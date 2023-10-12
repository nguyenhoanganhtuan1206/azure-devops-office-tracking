package com.openwt.officetracking.domain.device_history;

import com.openwt.officetracking.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class DeviceHistoryError {

    public static Supplier<NotFoundException> supplyDeviceHistoryNotFound(final String fieldName, final Object fieldValue) {
        return () -> new NotFoundException("Latest history of device with %s %s not found", fieldName, fieldValue);
    }
}
