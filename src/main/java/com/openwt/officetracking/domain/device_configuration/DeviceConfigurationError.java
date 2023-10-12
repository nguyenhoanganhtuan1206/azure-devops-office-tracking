package com.openwt.officetracking.domain.device_configuration;

import com.openwt.officetracking.error.BadRequestException;
import com.openwt.officetracking.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class DeviceConfigurationError {

    public static Supplier<NotFoundException> supplyDeviceConfigurationNotFound(final String fieldName, final Object fieldValue) {
        return () -> new NotFoundException("Device Configuration with %s %s not found", fieldName, fieldValue);
    }

    public static Supplier<BadRequestException> supplyDeviceConfigurationInvalid() {
        return () -> new BadRequestException("Please ensure you select a device configuration that aligns with your chosen type");
    }
}