package com.openwt.officetracking.persistent.position;

import com.openwt.officetracking.domain.position.Position;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.persistent.position.PositionEntityMapper.*;
import static org.apache.commons.collections4.IterableUtils.toList;

@Repository
@RequiredArgsConstructor
public class PositionStore {

    private final PositionRepository positionRepository;

    public List<Position> findAll() {
        return toPositions(toList(positionRepository.findAll()));
    }

    public Optional<Position> findById(final UUID uuid) {
        return positionRepository.findById(uuid)
                .map(PositionEntityMapper::toPosition);
    }

    public Optional<Position> findByName(final String name) {
        return positionRepository.findByName(name)
                .map(PositionEntityMapper::toPosition);
    }

    public Position save(final Position position) {
        return toPosition(positionRepository.save(toPositionEntity(position)));
    }

    public void delete(final UUID uuid) {
        positionRepository.deleteById(uuid);
    }
}
