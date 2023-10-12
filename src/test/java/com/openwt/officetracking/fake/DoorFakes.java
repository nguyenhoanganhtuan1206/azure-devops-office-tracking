package com.openwt.officetracking.fake;

import com.openwt.officetracking.domain.door.Door;
import com.openwt.officetracking.persistent.door.DoorEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.*;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class DoorFakes {

    public static Door buildDoor() {
        return Door.builder()
                .id(randomUUID())
                .name(randomAlphabetic(9))
                .major(randomInteger(500))
                .minor(randomInteger(500))
                .note(randomAlphabetic(9))
                .officeId(randomUUID())
                .createdAt(randomInstant())
                .build();
    }

    public static List<Door> buildDoors() {
        return buildList(DoorFakes::buildDoor);
    }

    public static DoorEntity buildDoorEntity() {
        return DoorEntity.builder()
                .id(randomUUID())
                .name(randomAlphabetic(9))
                .major(randomInteger(500))
                .minor(randomInteger(500))
                .note(randomAlphabetic(9))
                .officeId(randomUUID())
                .createdAt(randomInstant())
                .build();
    }

    public static List<DoorEntity> buildDoorEntities() {
        return buildList(DoorFakes::buildDoorEntity);
    }
}