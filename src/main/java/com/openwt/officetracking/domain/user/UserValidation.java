package com.openwt.officetracking.domain.user;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.openwt.officetracking.domain.contract_type.ContractType;
import com.openwt.officetracking.domain.gender.Gender;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.util.UUID;

import static com.openwt.officetracking.domain.utils.Base64Utils.isBase64;
import static com.openwt.officetracking.domain.utils.URLUtils.isValidUrl;
import static com.openwt.officetracking.error.CommonError.supplyValidationError;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@UtilityClass
public class UserValidation {

    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$";

    private static final String IDENTIFIER_REGEX = "^[0-9]\\d*$";

    private static final String PHONE_NUMBER_REGEX = "^[(]?[0-9]{3}[)]?[-\\\\s\\\\.]?[0-9]{3}[-\\\\s\\\\.]?[0-9]{4,6}$";

    private static final int NAME_MAX_LENGTH = 50;

    private static final int BASE64_MAX_LENGTH = 1024 * 1024 * 20;

    private static final String countryCodeSource = PhoneNumber.CountryCodeSource.FROM_DEFAULT_COUNTRY.name();

    public static void verifyPasswordFormat(final String password) {
        if (isBlank(password) || !password.matches(PASSWORD_REGEX)) {
            throw supplyValidationError("Invalid password").get();
        }
    }

    public static void validateUser(final User user) {
        validatePersonalEmail(user.getPersonalEmail());
        validateCompanyEmail(user.getCompanyEmail());
        validateFirstName(user.getFirstName());
        validateLastName(user.getLastName());
        validateContractType(user.getContractType());
        validateIdentifier(user.getIdentifier());
        validatePhoneNumber(user.getPhoneNumber());
        validatePositionId(user.getPositionId());
        validateGender(user.getGender());
        validateStartDate(user.getStartDate());
        validateEndDate(user.getEndDate(), user.getStartDate());
        validatePhoto(user.getPhoto());
    }

    public static void validateWithRoleUser(final User user) {
        validatePersonalEmail(user.getPersonalEmail());
        validateFirstName(user.getFirstName());
        validateLastName(user.getLastName());
        validateIdentifier(user.getIdentifier());
        validatePhoneNumber(user.getPhoneNumber());
        validateGender(user.getGender());
        validatePhoto(user.getPhoto());
    }

    public static void validateProfileRequestMobile(final User updateUserRequest) {
        validatePersonalEmail(updateUserRequest.getPersonalEmail());

        if (isNotBlank(updateUserRequest.getCompanyEmail())) {
            validateCompanyEmail(updateUserRequest.getCompanyEmail());
        }
        if (isNotBlank(updateUserRequest.getFirstName())) {
            validateFirstName(updateUserRequest.getFirstName());
        }
        if (isNotBlank(updateUserRequest.getLastName())) {
            validateLastName(updateUserRequest.getLastName());
        }

        validatePhoto(updateUserRequest.getPhoto());
        validatePhoneNumber(updateUserRequest.getPhoneNumber());
    }

    private static void validateGender(final Gender gender) {
        if (gender == null) {
            throw supplyValidationError("Gender cannot be blank").get();
        }
    }

    private static void validatePositionId(final UUID positionId) {
        if (positionId == null) {
            throw supplyValidationError("Position cannot be blank").get();
        }
    }

    private static void validateEndDate(final Instant endDate, final Instant startDate) {
        if (endDate != null) {
            if (endDate.compareTo(startDate) <= 0) {
                throw supplyValidationError("Start date must be before end date").get();
            }
        }
    }

    private static void validateStartDate(final Instant startDate) {
        if (startDate == null) {
            throw supplyValidationError("Start date cannot be null").get();
        }
    }

    private static void validatePhoneNumber(final String phoneNumber) {
        if (isNotBlank(phoneNumber)) {
            final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

            try {
                final PhoneNumber parsedNumber = phoneNumberUtil.parse(phoneNumber, countryCodeSource);
                if (!phoneNumberUtil.isValidNumber(parsedNumber)) {
                    throw supplyValidationError("Phone number is invalid").get();
                }
            } catch (NumberParseException e) {
                if (phoneNumber.matches(PHONE_NUMBER_REGEX)) {
                    return;
                }
                throw supplyValidationError("Invalid phone number format").get();
            }
        }
    }

    private static void validateIdentifier(final String identifier) {
        if (isNotBlank(identifier)) {
            if (!identifier.matches(IDENTIFIER_REGEX)) {
                throw supplyValidationError("Identifier is invalid").get();
            }
        }
    }

    private static void validateContractType(final ContractType contractType) {
        if (contractType == null) {
            throw supplyValidationError("Contract type cannot be blank").get();
        }
    }

    private static void validateLastName(final String lastName) {
        if (isBlank(lastName)) {
            throw supplyValidationError("Last name cannot be blank").get();
        }

        if (lastName.length() > NAME_MAX_LENGTH) {
            throw supplyValidationError("Last name cannot be over 50 characters").get();
        }
    }

    private static void validateFirstName(final String firstName) {
        if (isBlank(firstName)) {
            throw supplyValidationError("First name cannot be blank").get();
        }

        if (firstName.length() > NAME_MAX_LENGTH) {
            throw supplyValidationError("First name cannot be over 50 characters").get();
        }
    }

    private static void validatePersonalEmail(final String personalEmail) {
        if (isNotBlank(personalEmail)) {
            if (!personalEmail.matches(EMAIL_REGEX)) {
                throw supplyValidationError("Personal email is invalid format").get();
            }
        }
    }

    private static void validateCompanyEmail(final String companyEmail) {
        if (isBlank(companyEmail)) {
            throw supplyValidationError("Company email cannot be blank").get();
        }
        if (!companyEmail.matches(EMAIL_REGEX)) {
            throw supplyValidationError("Company email is invalid format").get();
        }
    }

    private static void validatePhoto(final String photo) {
        if (isBlank(photo)) {
            return;
        }

        if (isBase64(photo)) {
            if (photo.length() > BASE64_MAX_LENGTH) {
                throw supplyValidationError("The image size is too large. Please choose a smaller image.").get();
            }
            return;
        }

        if (!isValidUrl(photo)) {
            throw supplyValidationError("Image is invalid!").get();
        }
    }
}
