package com.openwt.officetracking.domain.device_configuration_value;

import com.openwt.officetracking.error.BadRequestException;
import lombok.experimental.UtilityClass;

import java.util.UUID;

import static com.openwt.officetracking.error.CommonError.supplyValidationError;
import static org.apache.commons.lang3.StringUtils.isBlank;

@UtilityClass
public class DeviceConfigurationValueValidation {

    private static final int VALUE_MAX_LENGTH = 50;

    public static void validateDeviceConfigurationRequest(final DeviceConfigurationValueRequest deviceConfigurationValueRequest) {
        validateModel(deviceConfigurationValueRequest.getModelId());
        validateConfiguration(deviceConfigurationValueRequest.getConfigurationId());
        validateValue(deviceConfigurationValueRequest.getValue());
    }

    private static void validateValue(final String value) {
        if (isBlank(value)) {
            throw supplyValidationError("Device configuration cannot be blank").get();
        }

        if (value.length() > VALUE_MAX_LENGTH) {
            throw new BadRequestException("Value cannot be over %d characters", VALUE_MAX_LENGTH);
        }
    }

    private static void validateConfiguration(final UUID configurationId) {
        if (configurationId == null) {
            throw supplyValidationError("Device configuration cannot be blank").get();
        }
    }

    private static void validateModel(final UUID modelId) {
        if (modelId == null) {
            throw supplyValidationError("Device model cannot be blank").get();
        }
    }
}
