package com.openwt.officetracking.persistent.door;

import com.openwt.officetracking.domain.door.Door;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class DoorEntityMapper {

    public static Door toDoor(final DoorEntity beacon) {
        return Door.builder()
                .id(beacon.getId())
                .name(beacon.getName())
                .major(beacon.getMajor())
                .minor(beacon.getMinor())
                .note(beacon.getNote())
                .officeId(beacon.getOfficeId())
                .createdAt(beacon.getCreatedAt())
                .build();
    }

    public static List<Door> toDoors(final List<DoorEntity> doorBeaconEntities) {
        return doorBeaconEntities.stream()
                .map(DoorEntityMapper::toDoor)
                .toList();
    }

    public static DoorEntity toDoorEntity(final Door door) {
        return DoorEntity.builder()
                .id(door.getId())
                .name(door.getName())
                .major(door.getMajor())
                .minor(door.getMinor())
                .note(door.getNote())
                .officeId(door.getOfficeId())
                .createdAt(door.getCreatedAt())
                .build();
    }
}
