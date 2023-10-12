package com.openwt.officetracking.persistent.position;

import com.openwt.officetracking.domain.position.Position;
import lombok.experimental.UtilityClass;

import java.util.List;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@UtilityClass
public class PositionEntityMapper {

    public static Position toPosition(final PositionEntity positionEntity) {
        return Position.builder()
                .id(positionEntity.getId())
                .name(positionEntity.getName())
                .isTemporary(positionEntity.isTemporary())
                .build();
    }

    public static PositionEntity toPositionEntity(final Position position) {
        return PositionEntity.builder()
                .id(position.getId())
                .name(position.getName())
                .isTemporary(position.isTemporary())
                .build();
    }

    public static List<Position> toPositions(final List<PositionEntity> positionEntities) {
        return emptyIfNull(positionEntities).stream()
                .map(PositionEntityMapper::toPosition)
                .toList();
    }
}
