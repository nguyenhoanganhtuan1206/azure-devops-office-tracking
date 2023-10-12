package com.openwt.officetracking.fake;

import com.openwt.officetracking.domain.position.Position;
import com.openwt.officetracking.persistent.position.PositionEntity;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class PositionFakes {
    public static PositionEntity buildPositionEntity() {
        return PositionEntity.builder()
                .id(randomUUID())
                .name(randomAlphabetic(9, 10))
                .isTemporary(true)
                .build();
    }
    public static Position buildPosition() {
        return Position.builder()
                .id(randomUUID())
                .name(randomAlphabetic(9, 10))
                .isTemporary(true)
                .build();
    }

    public static List<Position> buildPositions() {
        return IntStream
                .range(1, 5)
                .mapToObj(_ignored -> buildPosition())
                .toList();
    }

    public static List<PositionEntity> buildPositionEntities() {
        return IntStream
                .range(1, 5)
                .mapToObj(_ignored -> buildPositionEntity())
                .toList();
    }
}