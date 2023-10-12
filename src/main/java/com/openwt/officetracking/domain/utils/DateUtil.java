package com.openwt.officetracking.domain.utils;

import lombok.experimental.UtilityClass;

import java.time.Instant;

@UtilityClass
public class DateUtil {

    public static boolean isDateEqual(final Instant date, final Instant anotherDate) {
        if (date == null || anotherDate == null) {
            return false;
        }
        return !date.equals(anotherDate);
    }
}
