package com.openwt.officetracking.fake;

import com.openwt.officetracking.api.office.OfficeRequestDTO;
import com.openwt.officetracking.domain.office.Office;
import com.openwt.officetracking.persistent.office.OfficeEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.openwt.officetracking.fake.CommonFakes.buildList;
import static java.lang.Math.random;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@UtilityClass
public class OfficeFakes {

    public static Office buildOffice() {
        return Office.builder()
                .id(randomUUID())
                .officeUUID(randomUUID().toString())
                .name(randomAlphabetic(3, 10))
                .longitude(random())
                .latitude(random())
                .radius(random())
                .build();
    }

    public static List<Office> buildOffices() {
        return buildList(OfficeFakes::buildOffice);
    }

    public static OfficeEntity buildOfficeEntity() {
        return OfficeEntity.builder()
                .id(randomUUID())
                .officeUUID(randomUUID().toString())
                .name(randomAlphabetic(3, 10))
                .longitude(random())
                .latitude(random())
                .radius(random())
                .build();
    }

    public static List<OfficeEntity> buildOfficeEntities() {
        return buildList(OfficeFakes::buildOfficeEntity);
    }

    public static OfficeRequestDTO buildOfficeRequestDTO() {
        return OfficeRequestDTO.builder()
                .officeUUID(randomUUID().toString())
                .name(randomAlphabetic(3, 10))
                .longitude(random())
                .latitude(random())
                .radius(random())
                .build();
    }
}