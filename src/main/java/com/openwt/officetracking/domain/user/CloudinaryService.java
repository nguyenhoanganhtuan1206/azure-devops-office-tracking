package com.openwt.officetracking.domain.user;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import static com.openwt.officetracking.domain.utils.Base64Utils.decodeBase64;
import static java.util.Collections.emptyMap;


@Service
@RequiredArgsConstructor
@SuppressWarnings({"rawtypes"})
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadBase64(final String base64String) {
        return upload(decodeBase64(base64String));
    }

    public String upload(final byte[] image) {
        return internalUpload(image).get("secure_url").toString();
    }

    private Map internalUpload(final byte[] image) {
        try {
            return cloudinary.uploader().upload(image, emptyMap());
        } catch (IOException exception) {
            throw new RuntimeException("Failed to upload image", exception);
        }
    }
}
