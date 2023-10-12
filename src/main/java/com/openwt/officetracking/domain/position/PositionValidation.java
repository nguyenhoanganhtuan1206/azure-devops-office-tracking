package com.openwt.officetracking.domain.position;

import com.openwt.officetracking.error.BadRequestException;
import lombok.experimental.UtilityClass;

import static org.apache.commons.lang3.StringUtils.isBlank;

@UtilityClass
public class PositionValidation {

    public static void validate(final Position position) {
        if (isBlank(position.getName())) {
            throw new BadRequestException("Name is required, please check again");
        }
    }
}
