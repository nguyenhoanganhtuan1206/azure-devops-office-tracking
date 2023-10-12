package com.openwt.officetracking.api.office;

import com.openwt.officetracking.domain.office.Office;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class OfficeDTOMapper {

    public static Office toOffice(final OfficeRequestDTO officeRequestDTO) {
        return Office.builder()
                .officeUUID(officeRequestDTO.getOfficeUUID())
                .name(officeRequestDTO.getName())
                .longitude(officeRequestDTO.getLongitude())
                .latitude(officeRequestDTO.getLatitude())
                .radius(officeRequestDTO.getRadius())
                .build();
    }

    public static OfficeResponseDTO toOfficeResponseDTO(final Office office) {
        return OfficeResponseDTO.builder()
                .id(office.getId())
                .officeUUID(office.getOfficeUUID())
                .name(office.getName())
                .longitude(office.getLongitude())
                .latitude(office.getLatitude())
                .radius(office.getRadius())
                .build();
    }

    public static List<OfficeResponseDTO> toOfficeResponseDTOs(final List<Office> offices) {
        return offices.stream()
                .map(OfficeDTOMapper::toOfficeResponseDTO)
                .toList();
    }
}