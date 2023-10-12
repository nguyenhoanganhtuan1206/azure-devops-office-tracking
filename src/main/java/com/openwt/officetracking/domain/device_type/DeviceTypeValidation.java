package com.openwt.officetracking.domain.device_type;

import com.openwt.officetracking.error.BadRequestException;
import lombok.experimental.UtilityClass;

import static com.openwt.officetracking.error.CommonError.supplyValidationError;
import static org.apache.commons.lang3.StringUtils.isBlank;

@UtilityClass
public class DeviceTypeValidation {

    private static final int TYPE_MAX_LENGTH = 50;

    public static void validateDeviceType(final DeviceType deviceType) {
        validateName(deviceType.getName());
    }

    private static void validateName(final String name) {
        if (isBlank(name)) {
            throw supplyValidationError("Type name cannot be blank").get();
        }

        if (name.length() > TYPE_MAX_LENGTH) {
            throw new BadRequestException("Value cannot be over %d characters", TYPE_MAX_LENGTH);
        }
    }
}
