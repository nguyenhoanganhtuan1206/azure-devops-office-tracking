package com.openwt.officetracking.domain.office;

import com.openwt.officetracking.persistent.office.OfficeStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.domain.office.OfficeError.supplyOfficeNotFound;
import static com.openwt.officetracking.domain.office.OfficeValidation.validateOfficeInformation;

@Service
@RequiredArgsConstructor
public class OfficeService {

    private final OfficeStore officeStore;

    public List<Office> findAll() {
        return officeStore.findAll();
    }

    public Office findById(final UUID id) {
        return officeStore.findById(id)
                .orElseThrow(supplyOfficeNotFound("id", id));
    }

    public Office update(final UUID id, final Office officeUpdate) {
        validateOfficeInformation(officeUpdate);

        final Office currentOffice = findById(id);

        return officeStore.save(updateOfficeRegionProperties(currentOffice, officeUpdate));
    }

    private Office updateOfficeRegionProperties(final Office currentOffice, final Office updateOffice) {
        return currentOffice
                .withOfficeUUID(updateOffice.getOfficeUUID())
                .withName(updateOffice.getName())
                .withLongitude(updateOffice.getLongitude())
                .withLatitude(updateOffice.getLatitude())
                .withRadius(updateOffice.getRadius());
    }
}