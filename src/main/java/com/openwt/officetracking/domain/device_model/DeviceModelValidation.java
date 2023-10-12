package com.openwt.officetracking.domain.device_model;

import com.openwt.officetracking.error.BadRequestException;
import lombok.experimental.UtilityClass;

import java.util.UUID;

import static com.openwt.officetracking.error.CommonError.supplyValidationError;
import static org.apache.commons.lang3.StringUtils.isBlank;

@UtilityClass
public class DeviceModelValidation {

    private static final int MODEL_MAX_LENGTH = 50;

    public static void validateDeviceModel(final DeviceModel deviceModel) {
        validateType(deviceModel.getTypeId());
        validateModelName(deviceModel.getName());
    }

    private static void validateModelName(final String model) {
        if (isBlank(model)) {
            throw supplyValidationError("Model name cannot be blank").get();
        }

        if (model.length() > MODEL_MAX_LENGTH) {
            throw new BadRequestException("Model nam cannot be over %d characters", MODEL_MAX_LENGTH);
        }
    }

    private static void validateType(final UUID type) {
        if (type == null) {
            throw supplyValidationError("Device type cannot be blank").get();
        }
    }
}
