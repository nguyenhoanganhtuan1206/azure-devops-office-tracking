package com.openwt.officetracking.domain.position;

import com.openwt.officetracking.persistent.position.PositionStore;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.openwt.officetracking.domain.position.PositionError.supplyPositionAlreadyExist;
import static com.openwt.officetracking.domain.position.PositionError.supplyPositionIdNotFound;
import static com.openwt.officetracking.domain.position.PositionValidation.validate;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionStore positionStore;

    public List<Position> findAll() {
        return positionStore.findAll();
    }

    public Position findById(final UUID id) {
        return positionStore.findById(id)
                .orElseThrow(supplyPositionIdNotFound(id));
    }

    public Position create(final Position position) {
        validate(position);
        verifyPositionAvailable(position.getName());

        return positionStore.save(position);
    }

    public Position update(final UUID id, final Position position) {
        final Position currentPosition = findById(id);
        validate(position);

        if (!StringUtils.equals(position.getName(), currentPosition.getName())) {
            verifyPositionAvailable(position.getName());
            currentPosition.setName(position.getName());
        }

        currentPosition.setTemporary(position.isTemporary());

        return positionStore.save(currentPosition);
    }

    public void delete(final UUID id) {
        findById(id);

        positionStore.delete(id);
    }

    private void verifyPositionAvailable(final String name) {
        positionStore.findByName(name)
                .ifPresent(position -> {
                    throw supplyPositionAlreadyExist(position.getName()).get();
                });
    }
}
