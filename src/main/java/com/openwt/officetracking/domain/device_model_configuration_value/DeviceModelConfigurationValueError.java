package com.openwt.officetracking.domain.device_model_configuration_value;

import com.openwt.officetracking.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class DeviceModelConfigurationValueError {

    public static Supplier<NotFoundException> supplyDeviceModelConfigurationValueNotFound(final String fieldName, final Object fieldValue) {
        return () -> new NotFoundException("Device Model Configuration Value with %s %s not found", fieldName, fieldValue);
    }
}