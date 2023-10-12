package com.openwt.officetracking.domain.device;

import com.openwt.officetracking.domain.device_status.DeviceStatus;
import com.openwt.officetracking.error.BadRequestException;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.util.UUID;

import static com.openwt.officetracking.domain.device.DeviceError.supplyDeviceValidation;
import static com.openwt.officetracking.error.CommonError.supplyValidationError;
import static org.apache.commons.lang3.StringUtils.isBlank;

@UtilityClass
public class DeviceValidation {

    private static final int SERIAL_NUMBER_MAX_LENGTH = 30;

    private static final int CONDITION_NOTE_MAX_LENGTH = 250;

    public static void validateDeviceRequest(final DeviceRequest deviceRequest) {
        validateSerialNumber(deviceRequest.getSerialNumber());
        validateModel(deviceRequest.getDeviceTypeConfig().getModelId());
        validateDeviceTime(deviceRequest);
        validateCondition(deviceRequest.getCondition());
        validateNote(deviceRequest.getNote());
        validateDeviceStatus(deviceRequest);
    }

    public static void validateDeviceRequestRequest(final DeviceRequest deviceRequest) {
        validateModel(deviceRequest.getDeviceTypeConfig().getModelId());
        validateReason(deviceRequest.getCondition());
    }

    private static void validateSerialNumber(final String serialNumber) {
        if (isBlank(serialNumber)) {
            throw supplyValidationError("Serial Number cannot be blank").get();
        }

        if (serialNumber.length() > SERIAL_NUMBER_MAX_LENGTH) {
            throw supplyValidationError("Serial Number cannot be over 30 characters").get();
        }
    }

    private static void validateModel(final UUID modelId) {
        if (modelId == null) {
            throw supplyValidationError("Device model cannot be blank").get();
        }
    }

    private static void validateDeviceStatus(final DeviceRequest deviceRequest) {
        if (deviceRequest.getDeviceStatus() == DeviceStatus.UTILIZED) {
            throw supplyValidationError("Status UTILIZED is not available for this action").get();
        }

        if (deviceRequest.getDeviceStatus() == DeviceStatus.ASSIGNED && deviceRequest.getAssignUserId() == null) {
            throw supplyValidationError("You have to assign a user when the status is set to ASSIGNED").get();
        }
    }

    private void validateDeviceTime(final DeviceRequest deviceRequest) {
        final Instant currentTime = Instant.now();
        final Instant devicePurchaseAt = deviceRequest.getPurchaseAt();
        final Instant deviceWarrantyAt = deviceRequest.getWarrantyEndAt();

        if (devicePurchaseAt == null) {
            throw supplyValidationError("Purchase date cannot be blank").get();
        }

        if (devicePurchaseAt.isAfter(currentTime)) {
            throw supplyDeviceValidation("The device's Purchase date cannot be later than the current date.").get();
        }

        if (deviceWarrantyAt != null && deviceWarrantyAt.isBefore(devicePurchaseAt)) {
            throw supplyDeviceValidation("The device's Warranty Date must be later than the Purchase date.").get();
        }
    }

    private static void validateCondition(final String condition) {
        if (isBlank(condition)) {
            throw supplyValidationError("Condition cannot be blank").get();
        }

        if (condition.length() > CONDITION_NOTE_MAX_LENGTH) {
            throw new BadRequestException("Condition cannot be over %s characters", CONDITION_NOTE_MAX_LENGTH);
        }
    }

    private static void validateReason(final String reason) {
        if (isBlank(reason)) {
            throw supplyValidationError("Reason cannot be blank").get();
        }

        if (reason.length() > CONDITION_NOTE_MAX_LENGTH) {
            throw new BadRequestException("Reason cannot be over %s characters", CONDITION_NOTE_MAX_LENGTH);
        }
    }

    public static void validateNote(final String note) {
        if (!isBlank(note) && note.length() > CONDITION_NOTE_MAX_LENGTH) {
            throw new BadRequestException("Note cannot be over %s characters", CONDITION_NOTE_MAX_LENGTH);
        }
    }
}