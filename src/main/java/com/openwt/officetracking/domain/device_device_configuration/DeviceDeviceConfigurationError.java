package com.openwt.officetracking.domain.device_device_configuration;

import com.openwt.officetracking.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class DeviceDeviceConfigurationError {

    public static Supplier<NotFoundException> supplyDeviceDeviceConfigurationNotFound(final String fieldName, final Object fieldValue) {
        return () -> new NotFoundException("Device Device Configuration with %s %s not found", fieldName, fieldValue);
    }
}