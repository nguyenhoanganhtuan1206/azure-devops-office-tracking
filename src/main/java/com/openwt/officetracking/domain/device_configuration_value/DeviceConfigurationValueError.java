package com.openwt.officetracking.domain.device_configuration_value;

import com.openwt.officetracking.error.BadRequestException;
import com.openwt.officetracking.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class DeviceConfigurationValueError {

    public static Supplier<NotFoundException> supplyDeviceConfigurationValueNotFound(final String fieldName, final Object fieldValue) {
        return () -> new NotFoundException("Device Configuration Value with %s %s not found", fieldName, fieldValue);
    }

    public static Supplier<BadRequestException> supplyDeviceConfigurationValueInvalid() {
        return () -> new BadRequestException("Your configuration value does not match the selected configuration.");
    }

    public static Supplier<BadRequestException> supplyValueNameExisted(final String fieldName, final Object fieldValue) {
        return () -> new BadRequestException("Value with %s %s already exists", fieldName, fieldValue);
    }
}