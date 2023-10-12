package com.openwt.officetracking.persistent.office;

import com.openwt.officetracking.domain.office.Office;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.persistent.office.OfficeEntityMapper.toOffices;
import static com.openwt.officetracking.persistent.office.OfficeEntityMapper.*;
import static org.apache.commons.collections4.IterableUtils.toList;

@Repository
@RequiredArgsConstructor
public class OfficeStore {

    private final OfficeRepository officeRepository;

    public List<Office> findAll() {
        return toOffices(toList(officeRepository.findAll()));
    }

    public Optional<Office> findById(final UUID id) {
        return officeRepository.findById(id)
                .map(OfficeEntityMapper::toOffice);
    }

    public Office save(final Office office) {
        return toOffice(officeRepository.save(toOfficeEntity(office)));
    }
}