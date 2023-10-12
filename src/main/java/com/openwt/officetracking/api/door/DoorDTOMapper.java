package com.openwt.officetracking.api.door;

import com.openwt.officetracking.domain.door.Door;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class DoorDTOMapper {

    public static Door toDoor(final DoorRequestDTO doorRequestDTO) {
        return Door.builder()
                .name(doorRequestDTO.getName())
                .major(doorRequestDTO.getMajor())
                .minor(doorRequestDTO.getMinor())
                .note(doorRequestDTO.getNote())
                .officeId(doorRequestDTO.getOfficeId())
                .build();
    }

    public static DoorResponseDTO toDoorDTO(final Door door) {
        return DoorResponseDTO.builder()
                .id(door.getId())
                .name(door.getName())
                .major(door.getMajor())
                .minor(door.getMinor())
                .note(door.getNote())
                .officeId(door.getOfficeId())
                .createdAt(door.getCreatedAt())
                .build();
    }

    public static List<DoorResponseDTO> toDoorDTOs(final List<Door> doors) {
        return doors.stream()
                .map(DoorDTOMapper::toDoorDTO)
                .toList();
    }
}