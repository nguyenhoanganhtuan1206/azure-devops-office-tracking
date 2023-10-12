package com.openwt.officetracking.domain.utils;

import lombok.experimental.UtilityClass;

import java.net.MalformedURLException;
import java.net.URL;

@UtilityClass
public class URLUtils {

    public static boolean isValidUrl(final String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
