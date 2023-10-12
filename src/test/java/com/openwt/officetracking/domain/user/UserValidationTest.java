package com.openwt.officetracking.domain.user;

import com.openwt.officetracking.error.BadRequestException;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.util.Base64;

import static com.openwt.officetracking.domain.user.UserValidation.*;
import static com.openwt.officetracking.fake.UserFakes.buildUser;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserValidationTest {

    @Test
    void validate_OK() {
        final var user = buildUser();

        assertDoesNotThrow(() -> validateUser(user));
    }

    @Test
    void validateUpdateProfileRequestMobile_OK() {
        final var user = buildUser();

        assertDoesNotThrow(() -> validateProfileRequestMobile(user));
    }

    @Test
    void validate_ThrowEmailInvalidFormat() {
        final var user = buildUser()
                .withPersonalEmail("anhtuan");

        assertThrows(BadRequestException.class, () -> validateUser(user));
    }

    @ParameterizedTest
    @CsvSource({
            "123456789a0",
            "12345678a"
    })
    void validate_ThrowInvalidIdentifier(final String identifier) {
        final var user = buildUser()
                .withIdentifier(identifier);

        assertThrows(BadRequestException.class, () -> validateUser(user));
    }

    @ParameterizedTest
    @CsvSource({
            "+09876543210",
            "a1987654321",
            "198765432a"
    })
    void validate_ThrowInvalidPhoneNumber(final String phoneNumber) {
        final var user = buildUser()
                .withPhoneNumber(phoneNumber);

        assertThrows(BadRequestException.class, () -> validateUser(user));
    }

    @Test
    void validate_ThrowContractTypeEmpty() {
        final var user = buildUser()
                .withContractType(null);

        assertThrows(BadRequestException.class, () -> validateUser(user));
    }

    @Test
    void validate_ThrowStartDateNull() {
        final var user = buildUser()
                .withStartDate(null);

        assertThrows(BadRequestException.class, () -> validateUser(user));
    }

    @Test
    void validate_ThrowStartDateAfterEndDate() {
        final var user = buildUser();
        final var startDate = user.getStartDate();

        user.setEndDate(startDate.minus(Duration.ofDays(1)));

        assertThrows(BadRequestException.class, () -> validateUser(user));
    }

    @Test
    void validate_ThrowFirstNameEmpty() {
        final var user = buildUser()
                .withFirstName(null);

        assertThrows(BadRequestException.class, () -> validateUser(user));
    }

    @Test
    void validateFirstName_OK() {
        final var user = buildUser()
                .withFirstName("Ạ à á ã");

        assertDoesNotThrow(() -> validateUser(user));
    }

    @Test
    void validate_ThrowFirstNameInvalidLength() {
        final var user = buildUser()
                .withFirstName(randomAlphabetic(51));

        assertThrows(BadRequestException.class, () -> validateUser(user));
    }

    @Test
    void validate_ThrowLastNameEmpty() {
        final var user = buildUser()
                .withLastName(null);

        assertThrows(BadRequestException.class, () -> validateUser(user));
    }

    @Test
    void validate_ThrowLastNameInvalidLength() {
        final var user = buildUser()
                .withLastName(randomAlphabetic(51));

        assertThrows(BadRequestException.class, () -> validateUser(user));
    }

    @Test
    void validate_ThrowPositionEmpty() {
        final var user = buildUser()
                .withPositionId(null);

        assertThrows(BadRequestException.class, () -> validateUser(user));
    }

    @Test
    void validate_ThrowGenderEmpty() {
        final var user = buildUser()
                .withGender(null);

        assertThrows(BadRequestException.class, () -> validateUser(user));
    }

    @Test
    void verifyPasswordEmpty_ThrowBadRequest() {
        assertThrows(BadRequestException.class, () -> verifyPasswordFormat(null));
    }

    @Test
    void verifyNewPassword_NotThrown() {
        final var password = "12345Aa!";

        assertDoesNotThrow(() -> verifyPasswordFormat(password));
    }

    @ParameterizedTest
    @CsvSource({
            "1234567",
            "123456a!",
            "123456A!",
            "123456aA",
            "newPassword!"
    })
    void verifyNewPassword_ThrowError(final String password) {
        assertThrows(BadRequestException.class, () -> verifyPasswordFormat(password));
    }

    @Test
    void verifyEnteredPhoto_ThrowInvalidBase64Error() {
        final byte[] bytes = RandomUtils.nextBytes(1025 * 1025 * 25);
        final String base64 = String.format("%s,%s", "data:image/png;base64", Base64.getEncoder().encodeToString(bytes));
        final var user = buildUser().withPhoto(base64);
        assertThrows(BadRequestException.class, () -> validateUser(user));
    }

    @Test
    void verifyEnteredPhoto_ThrowInvalidURLError() {
        final var url = randomAlphabetic(10, 20);
        final var user = buildUser().withPhoto(url);
        assertThrows(BadRequestException.class, () -> validateUser(user));
    }

    @ParameterizedTest
    @CsvSource({
            "+355 68 433 9826",
            "+1 202-918-2132",
            "+1 939-744-0314",
            "+84 559 095 144",
            "0935996259",
            "(+84) 935 996 259",
            "081.748.1818"
    })
    void validatePhoneNumber_OK(final String phoneNumber) {
        final var user = buildUser()
                .withPhoneNumber(phoneNumber);

        assertDoesNotThrow(() -> validateUser(user));
    }
}
