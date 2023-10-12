package com.openwt.officetracking.persistent.image;

import com.openwt.officetracking.domain.image.Image;
import com.openwt.officetracking.domain.user.User;
import com.openwt.officetracking.persistent.user.UserEntity;
import com.openwt.officetracking.persistent.user.UserEntityMapper;

import java.util.List;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

public class ImageEntityMapper {

    public static Image toImage(final ImageEntity imageEntity) {
        return Image.builder()
                .id(imageEntity.getId())
                .url(imageEntity.getUrl())
                .userId(imageEntity.getUserId())
                .build();
    }

    public static List<Image> toImages(final List<ImageEntity> imageEntities) {
        return emptyIfNull(imageEntities)
                .stream()
                .map(ImageEntityMapper::toImage)
                .toList();
    }

    public static ImageEntity toImageEntity(final Image image) {
        return ImageEntity.builder()
                .id(image.getId())
                .url(image.getUrl())
                .userId(image.getUserId())
                .build();
    }
}
