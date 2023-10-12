package com.openwt.officetracking.domain.utils;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.openwt.officetracking.error.BadRequestException;
import lombok.experimental.UtilityClass;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

import java.io.IOException;

import static com.google.zxing.BarcodeFormat.QR_CODE;
import static org.apache.logging.log4j.util.Strings.isBlank;

@UtilityClass
public class QRCodeGenerator {

    private final QRCodeWriter qrCodeWriter = new QRCodeWriter();

    public static byte[] generateImageQRCode(final String value, final int width, final int height) throws IOException, WriterException {
        validateQRCodeValue(value);

        final BitMatrix bitMatrix = qrCodeWriter.encode(value, QR_CODE, width, height);

        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            outputStream.flush();

            return outputStream.toByteArray();
        }
    }

    private void validateQRCodeValue(final String value) {
        if (isBlank(value)) {
            throw new BadRequestException("Value must not be null or empty, please check again");
        }
    }
}
