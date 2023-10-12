package com.openwt.officetracking.domain.utils;


import lombok.experimental.UtilityClass;

import java.util.Base64;

import static com.openwt.officetracking.domain.utils.StringUtils.findCaptureGroup;

@UtilityClass
public class Base64Utils {

    private static final Base64.Decoder decoder = Base64.getDecoder();

    private static final String BASE64_HEADER = "(data:image\\/(png|jpeg|jpg|gif|bmp);base64,).*";

    public static boolean isBase64(final String text) {
        return text.matches(BASE64_HEADER);
    }

    public static byte[] decodeBase64(final String text) {
        return decoder.decode(text.substring(findCaptureGroup(text, BASE64_HEADER).length()));
    }

}
