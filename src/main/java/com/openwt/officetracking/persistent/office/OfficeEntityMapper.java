package com.openwt.officetracking.persistent.office;

import com.openwt.officetracking.domain.office.Office;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class OfficeEntityMapper {

    public static Office toOffice(final OfficeEntity officeEntity) {
        return Office.builder()
                .id(officeEntity.getId())
                .officeUUID(officeEntity.getOfficeUUID())
                .name(officeEntity.getName())
                .latitude(officeEntity.getLatitude())
                .longitude(officeEntity.getLongitude())
                .radius(officeEntity.getRadius())
                .build();
    }

    public static List<Office> toOffices(final List<com.openwt.officetracking.persistent.office.OfficeEntity> officeEntities) {
        return officeEntities.stream()
                .map(OfficeEntityMapper::toOffice)
                .toList();
    }

    public static OfficeEntity toOfficeEntity(final Office office) {
        return OfficeEntity.builder()
                .id(office.getId())
                .officeUUID(office.getOfficeUUID())
                .name(office.getName())
                .longitude(office.getLongitude())
                .latitude(office.getLatitude())
                .radius(office.getRadius())
                .build();
    }
}