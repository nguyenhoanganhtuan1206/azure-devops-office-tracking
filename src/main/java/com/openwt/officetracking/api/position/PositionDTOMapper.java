package com.openwt.officetracking.api.position;

import com.openwt.officetracking.domain.position.Position;
import lombok.experimental.UtilityClass;

import java.util.List;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@UtilityClass
public class PositionDTOMapper {

    public static PositionResponseDTO toPositionResponseDTO(final Position position) {
        return PositionResponseDTO.builder()
                .id(position.getId())
                .name(position.getName())
                .isTemporary(position.isTemporary())
                .build();
    }

    public static Position toPosition(final PositionRequestDTO positionRequestDTO) {
        return Position.builder()
                .name(positionRequestDTO.getName())
                .isTemporary(positionRequestDTO.isTemporary())
                .build();
    }

    public static List<PositionResponseDTO> toPositionResponseDTOs(final List<Position> positions) {
        return emptyIfNull(positions)
                .stream()
                .map(PositionDTOMapper::toPositionResponseDTO)
                .toList();
    }
}