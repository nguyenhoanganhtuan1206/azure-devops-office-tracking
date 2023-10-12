package com.openwt.officetracking.persistent.image;

import com.openwt.officetracking.domain.image.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.openwt.officetracking.persistent.image.ImageEntityMapper.toImage;
import static com.openwt.officetracking.persistent.image.ImageEntityMapper.toImageEntity;

@Repository
@RequiredArgsConstructor
public class ImageStore {

    private final ImageRepository imageRepository;

    public Image save(final Image image) {
        return toImage(imageRepository.save(toImageEntity(image)));
    }
}
