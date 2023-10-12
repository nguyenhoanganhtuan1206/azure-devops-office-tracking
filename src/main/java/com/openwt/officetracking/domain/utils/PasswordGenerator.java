package com.openwt.officetracking.domain.utils;

import lombok.experimental.UtilityClass;

import java.util.List;

import static com.google.common.primitives.Chars.asList;
import static java.util.Collections.shuffle;
import static org.apache.commons.lang3.RandomStringUtils.*;
import static org.apache.commons.lang3.StringUtils.join;

@UtilityClass
public class PasswordGenerator {

    private static final String SPECIAL_CHARACTERS_RANDOM = "!@#$%^&*";

    public static String generatePassword() {
        final String upperCaseLetter = random(1, 'A', 'Z', true, true);
        final String lowerCaseLetter = random(1, 'a', 'z', true, true);
        final String specialChar = random(1, SPECIAL_CHARACTERS_RANDOM);
        final String number = randomNumeric(1);
        final String randomChars = randomAlphanumeric(4);

        final String password = upperCaseLetter
                .concat(lowerCaseLetter)
                .concat(specialChar)
                .concat(number)
                .concat(randomChars);

        final List<Character> pwdChars = asList(password.toCharArray());
        shuffle(pwdChars);

        return join(pwdChars, "");
    }
}
